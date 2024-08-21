import osmnx as ox
from neo4j import GraphDatabase, basic_auth
import time

NEO4J_URI = "bolt://localhost:7687"
NEO4J_USER = "neo4j"
NEO4J_PASSWORD = "ogv27112002"

# Подключение к базе данных Neo4j
try:
    driver = GraphDatabase.driver(NEO4J_URI, auth=basic_auth(NEO4J_USER, NEO4J_PASSWORD))
    print("Connected to Neo4j successfully!")
except Exception as e:
    print(f"Failed to connect to Neo4j: {e}")
    driver = None

# Создание графа OSMNx
try:
    G_walk = ox.graph_from_place("Barnaul, Altai Krai, Russia", network_type="walk", simplify=False)
    G_drive = ox.graph_from_place("Barnaul, Altai Krai, Russia", network_type="drive_service", simplify=False)
    gdf_walk_nodes, gdf_walk_relationships = ox.graph_to_gdfs(G_walk)
    gdf_drive_nodes, gdf_drive_relationships = ox.graph_to_gdfs(G_drive)
    gdf_walk_nodes.reset_index(inplace=True)
    gdf_walk_relationships.reset_index(inplace=True)
    gdf_drive_nodes.reset_index(inplace=True)
    gdf_drive_relationships.reset_index(inplace=True)

    # Добавление префиксов
    gdf_walk_nodes['osmid'] = 'walk_' + gdf_walk_nodes['osmid'].astype(str)
    gdf_walk_relationships['u'] = 'walk_' + gdf_walk_relationships['u'].astype(str)
    gdf_walk_relationships['v'] = 'walk_' + gdf_walk_relationships['v'].astype(str)

    gdf_drive_nodes['osmid'] = 'drive_' + gdf_drive_nodes['osmid'].astype(str)
    gdf_drive_relationships['u'] = 'drive_' + gdf_drive_relationships['u'].astype(str)
    gdf_drive_relationships['v'] = 'drive_' + gdf_drive_relationships['v'].astype(str)

    print("OSMNx graphs created successfully!")
except Exception as e:
    print(f"Failed to create or plot OSMNx graphs: {e}")

# Создание ограничений и индексов
constraint_query = "CREATE CONSTRAINT IF NOT EXISTS FOR (i:Intersection) REQUIRE i.osmid IS UNIQUE"
walk_rel_index_query = "CREATE INDEX IF NOT EXISTS FOR ()-[r:WALK_SEGMENT]-() ON r.osmid"
road_rel_index_query = "CREATE INDEX IF NOT EXISTS FOR ()-[r:ROAD_SEGMENT]-() ON r.osmid"
address_constraint_query = "CREATE CONSTRAINT IF NOT EXISTS FOR (a:Address) REQUIRE a.id IS UNIQUE"
point_index_query = "CREATE POINT INDEX IF NOT EXISTS FOR (i:Intersection) ON i.location"

# Вставка данных
node_query = '''
    UNWIND $rows AS row
    WITH row WHERE row.osmid IS NOT NULL
    MERGE (i:Intersection {osmid: row.osmid})
        SET i.location = point({latitude: row.y, longitude: row.x}),
            i.ref = row.ref,
            i.highway = row.highway,
            i.street_count = toInteger(row.street_count)
    RETURN COUNT(*) as total
    '''

transport_rels_query = '''
    UNWIND $rows AS road
    MATCH (u:Intersection {osmid: road.u})
    MATCH (v:Intersection {osmid: road.v})
    MERGE (u)-[r:ROAD_SEGMENT {osmid: road.osmid}]->(v)
        SET r.oneway = road.oneway,
            r.lanes = road.lanes,
            r.ref = road.ref,
            r.name = road.name,
            r.highway = road.highway,
            r.max_speed = road.maxspeed,
            r.length = toFloat(road.length)
    RETURN COUNT(*) AS total
    '''
footway_rels_query = '''
    UNWIND $rows AS road
    WITH road WHERE road.highway IN ['service', 'pedestrian', 'footway', 'sidewalk', 'path', 'crossing', 'steps', 'road', 'track', 'living_street', 'residential', 'unclassified']
    MATCH (u:Intersection {osmid: road.u})
    MATCH (v:Intersection {osmid: road.v})
    MERGE (u)-[r:WALK_SEGMENT {osmid: road.osmid}]->(v)
        SET r.oneway = road.oneway,
            r.lanes = road.lanes,
            r.ref = road.ref,
            r.name = road.name,
            r.sidewalk = road.sidewalk,
            r.highway = road.highway,
            r.max_speed = road.maxspeed,
            r.length = toFloat(road.length)
    RETURN COUNT(*) AS total
    '''

def create_constraints(tx):
    tx.run(constraint_query)
    tx.run(walk_rel_index_query)
    tx.run(road_rel_index_query)
    tx.run(address_constraint_query)
    tx.run(point_index_query)

def insert_data(tx, query, rows, batch_size=3000, max_records=None):
    total = 0
    batch = 0
    if max_records:
        rows = rows[:max_records]
    while batch * batch_size < len(rows):
        results = tx.run(query, parameters={
            'rows': rows[batch * batch_size:(batch + 1) * batch_size].to_dict('records')}).data()
        print(results)
        total += results[0]['total']
        batch += 1
    return total

if driver:
    try:
        with driver.session() as session:
            session.execute_write(create_constraints)
            total_nodes_walk = session.execute_write(insert_data, node_query, gdf_walk_nodes.drop(columns=['geometry']))
            total_nodes_drive = session.execute_write(insert_data, node_query, gdf_drive_nodes.drop(columns=['geometry']))
            total_transport_rels = session.execute_write(insert_data, transport_rels_query, gdf_drive_relationships.drop(columns=['geometry']))
            total_footway_rels = session.execute_write(insert_data, footway_rels_query, gdf_walk_relationships.drop(columns=['geometry']))
            print(f"Total walk nodes inserted: {total_nodes_walk}")
            print(f"Total drive nodes inserted: {total_nodes_drive}")
            print(f"Total transport relationships inserted: {total_transport_rels}")
            print(f"Total footway relationships inserted: {total_footway_rels}")
    except Exception as e:
        print(f"Failed to write data to Neo4j: {e}")
