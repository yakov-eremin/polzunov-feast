from flask import current_app as app
from flask import render_template, request, jsonify
from .graph import G_walk, G_drive
from .geocode import fetch_address_details, save_address_to_neo4j
from .graph import convert_path_to_route_json, load_graphs, find_shortest_path

WALK_SPEED = 1.4
DRIVE_SPEED = 7.9

def process_route_request(data):
    source = data.get('source')
    target = data.get('target')
    route_type = data['route_type']

    if isinstance(source, str):
        source_details = fetch_address_details(source)
        if not source_details:
            return None, "Не удалось найти координаты для исходного адреса.", 400
        source_lat, source_lon, _ = source_details
    else:
        source_lat, source_lon = source

    if isinstance(target, str):
        target_details = fetch_address_details(target)
        if not target_details:
            return None, "Не удалось найти координаты для целевого адреса.", 400
        target_lat, target_lon, _ = target_details
    else:
        target_lat, target_lon = target

    graph = G_walk if route_type == 'walk' else G_drive

    def get_nearest_node_id(graph, lat, lon):
        min_dist = float("inf")
        nearest_node_id = None
        for node, data in graph.items():
            if data["pos"] is not None:
                dist = ((data["pos"][0] - lon) ** 2 + (data["pos"][1] - lat) ** 2) ** 0.5
                if dist < min_dist:
                    min_dist = dist
                    nearest_node_id = node
        return nearest_node_id

    source_id = get_nearest_node_id(graph, source_lat, source_lon)
    target_id = get_nearest_node_id(graph, target_lat, target_lon)

    if source_id is None or target_id is None:
        return None, "Не удалось найти узлы, соответствующие указанным координатам", 400

    shortest_path, path_length = find_shortest_path(graph, source_id, target_id, route_type)
    if not shortest_path:
        return None, f"No path found between nodes {source_id} and {target_id}.", 400

    average_speed = WALK_SPEED if route_type == 'walk' else DRIVE_SPEED
    travel_time_seconds = path_length / average_speed
    travel_time_minutes = travel_time_seconds / 60

    return (graph, shortest_path, path_length, travel_time_minutes), None, 200

@app.route('/')
def index():
    return render_template('map.html')

@app.route('/find_route', methods=['POST'])
def find_route():
    data = request.json
    result, error, status_code = process_route_request(data)
    if error:
        return jsonify({"error": error}), status_code

    graph, shortest_path, path_length, travel_time_minutes = result
    route_json = convert_path_to_route_json(graph, shortest_path, path_length, travel_time_minutes)
    return route_json

@app.route('/find_route_simple', methods=['POST'])
def find_route_simple():
    data = request.json
    result, error, status_code = process_route_request(data)
    if error:
        return jsonify({"error": error}), status_code

    _, _, path_length, travel_time_minutes = result

    return jsonify({
        "length": path_length,
        "travel_time": travel_time_minutes
    })

@app.route('/geocode', methods=['POST'])
def geocode():
    data = request.json
    address = data.get('address')
    if not address:
        return jsonify({"error": "Адрес не указан."}), 400

    address_details = fetch_address_details(address)
    if not address_details:
        return jsonify({"error": "Не удалось геокодировать адрес."}), 400

    latitude, longitude, full_address = address_details
    save_address_to_neo4j(address, latitude, longitude, full_address)

    response = {
        "message": "Адрес успешно геокодирован и сохранен в базе данных.",
        "coordinates": {
            "latitude": latitude,
            "longitude": longitude
        }
    }
    return jsonify(response)