import 'dart:convert';

import 'package:feast_mobile/models/route.dart';
import 'package:feast_mobile/models/route_info.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:latlong2/latlong.dart';

abstract class RoutingService {
  static const String baseUrl = '10.0.2.2:8081';

  static Future<RouteModel> getRouteFullFromAddress(
      String startAddress, String endAddress, RouteType type) async {
    final Response response = await http.post(Uri.http(baseUrl, '/find_route'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(<String, String?>{
          "source": startAddress,
          "target": endAddress,
          "route_type": type == RouteType.walk ? "walk" : "drive"
        }));

    return RouteModel.fromJson(jsonDecode(response.body));
  }

  static Future<RouteModel> getRouteFullFromCoord(
      LatLng startAddress, String endAddress, RouteType type) async {
    final Response response = await http.post(Uri.http(baseUrl, '/find_route'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(<String, String?>{
          "source": "${startAddress.latitude}, ${startAddress.longitude}",
          "target": endAddress,
          "route_type": type == RouteType.walk ? "walk" : "drive"
        }));

    return RouteModel.fromJson(jsonDecode(response.body));
  }

  static Future<RouteModel> getRouteBasicFromAddress(
      String startAddress, String endAddress, RouteType type) async {
    final Response response = await http.post(Uri.http(baseUrl, '/find_route'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(<String, String?>{
          "source": startAddress,
          "target": endAddress,
          "route_type": type == RouteType.walk ? "walk" : "drive"
        }));

    return RouteModel.fromJson(jsonDecode(response.body));
  }

  static Future<RouteModel> getRouteBasicFromCoord(
      LatLng startAddress, String endAddress, RouteType type) async {
    final Response response = await http.post(Uri.http(baseUrl, '/find_route'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(<String, String?>{
          "source": "${startAddress.latitude}, ${startAddress.longitude}",
          "target": endAddress,
          "route_type": type == RouteType.walk ? "walk" : "drive"
        }));

    return RouteModel.fromJson(jsonDecode(response.body));
  }
}
