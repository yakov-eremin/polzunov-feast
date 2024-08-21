import 'package:feast_mobile/models/route_info.dart';
import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';

class Map extends StatelessWidget {
  const Map({
    super.key,
    required this.routeInfo,
    required this.walkRouteShowing,
  });

  final RouteInfo routeInfo;
  final bool walkRouteShowing;

  @override
  Widget build(BuildContext context) {
    return FlutterMap(
      options: const MapOptions(
        //Центр Барнаула
        initialCenter: LatLng(
          53.3451806,
          83.7761902,
        ),
        initialZoom: 11,
        crs: Epsg3857(),
      ),
      children: [
        TileLayer(
          urlTemplate: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          userAgentPackageName: 'com.example.app',
        ),
        PolylineLayer(polylines: [
          Polyline(
            color: walkRouteShowing ? Colors.green : Colors.green.shade900,
            borderColor: Colors.black,
            borderStrokeWidth: 1,
            strokeWidth: 4,
            points: walkRouteShowing
                ? routeInfo.walk.coordinates!
                : routeInfo.car.coordinates!,
          )
        ]),
        MarkerLayer(markers: [
          Marker(
            point: routeInfo.car.coordinates!.first,
            child: Container(
              decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white, width: 2)),
              child: CircleAvatar(
                radius: 10,
                backgroundColor: Colors.green.shade900,
                foregroundColor: Colors.white,
                child: const Text('А'),
              ),
            ),
          ),
          Marker(
            point: routeInfo.car.coordinates!.last,
            child: Container(
              decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white, width: 2)),
              child: CircleAvatar(
                radius: 10,
                backgroundColor: Colors.green.shade900,
                foregroundColor: Colors.white,
                child: const Text('Б'),
              ),
            ),
          ),
        ]),
      ],
    );
  }
}
