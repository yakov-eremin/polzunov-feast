import 'package:latlong2/latlong.dart';

class RouteModel {
  final double length;
  final double time;
  final List<LatLng>? coordinates;

  RouteModel({
    required this.length,
    required this.time,
    required this.coordinates,
  });

  factory RouteModel.fromJson(Map<String, dynamic> json) {
    final temp = json['coordinates'] as List<dynamic>?;

    return RouteModel(
        length: json['length'] as double,
        time: json['travel_time'] as double,
        coordinates: temp != null
            ? (temp).map((item) {
                final temp = (item as Map<String, dynamic>);
                return LatLng(temp['lat'], temp['lon']);
              }).toList()
            : null);
  }
}
