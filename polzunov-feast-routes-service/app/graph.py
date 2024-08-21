from neo4j import GraphDatabase, basic_auth
import heapq, json
from app.config import Config

driver = None
G_walk = {}
G_drive = {}

def init_driver():
    global driver
    driver = GraphDatabase.driver(Config.NEO4J_URI, auth=basic_auth(Config.NEO4J_USER, Config.NEO4J_PASSWORD))

def load_graphs():
    global G_walk, G_drive
    init_driver()
    with driver.session() as session:
        result = session.run("MATCH (n:Intersection) RETURN n.osmid AS osmid, n.location AS location")
        for record in result:
            osmid = record["osmid"]
            location = record["location"]
            if osmid.startswith('walk_'):
                G_walk[osmid] = {"pos": (location.x, location.y), "edges": []}
            elif osmid.startswith('drive_'):
                G_drive[osmid] = {"pos": (location.x, location.y), "edges": []}

        result = session.run(
            """
            MATCH (a:Intersection)-[r:WALK_SEGMENT]->(b:Intersection)
            RETURN a.osmid AS source, b.osmid AS target, r.length AS length, r.highway AS highway
            """
        )
        for record in result:
            length = record["length"]
            G_walk[record["source"]]["edges"].append({
                "target": record["target"],
                "weight": length,
                "length": length
            })

        result = session.run(
            """
            MATCH (a:Intersection)-[r:ROAD_SEGMENT]->(b:Intersection)
            RETURN a.osmid AS source, b.osmid AS target, r.length AS length, r.highway AS highway
            """
        )
        for record in result:
            length = record["length"]
            highway_type = record["highway"]

            if highway_type in ['motorway', 'trunk', 'motorway_link', 'trunk_link']:
                weight = length * 0.25
            elif highway_type in ['primary', 'primary_link']:
                weight = length * 0.5
            elif highway_type in ['secondary', 'secondary_link']:
                weight = length * 0.75
            elif highway_type in ['tertiary', 'tertiary_link']:
                weight = length
            else:
                weight = length * 1.25

            G_drive[record["source"]]["edges"].append({
                "target": record["target"],
                "weight": weight,
                "length": length
            })

def find_shortest_path(graph, source, target, route_type):
    queue = [(0, source, 0)]
    distances = {node: float('inf') for node in graph}
    previous_nodes = {node: None for node in graph}
    distances[source] = 0
    path_length = 0

    while queue:
        current_distance, current_node, current_length = heapq.heappop(queue)

        if current_node == target:
            path_length = current_length
            break

        if current_distance > distances[current_node]:
            continue

        for edge in graph[current_node]["edges"]:
            neighbor = edge["target"]
            weight = edge["weight"]
            length = edge["length"]
            distance = current_distance + weight
            accumulated_length = current_length + length

            if distance < distances[neighbor]:
                distances[neighbor] = distance
                previous_nodes[neighbor] = current_node
                heapq.heappush(queue, (distance, neighbor, accumulated_length))

    path = []
    while target is not None:
        path.append(target)
        target = previous_nodes[target]
    path.reverse()

    if path[0] == source:
        return path, path_length
    else:
        return [], 0

def convert_path_to_route_json(graph, path, length, time):
    coordinates = []
    for node in path:
        pos = graph[node]['pos']
        coordinates.append({"lat": pos[1], "lon": pos[0]})

    route_json = {
        "length": length,
        "travel_time": time,
        "coordinates": coordinates
    }

    return route_json


