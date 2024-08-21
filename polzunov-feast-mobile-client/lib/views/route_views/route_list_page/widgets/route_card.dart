import 'package:feast_mobile/models/route_info.dart';
import 'package:flutter/material.dart';

class RouteCard extends StatefulWidget {
  const RouteCard({
    super.key,
    required this.routeInfo,
    this.onRouteButtonTap,
  });

  final RouteInfo routeInfo;
  final void Function()? onRouteButtonTap;

  @override
  State<RouteCard> createState() => _RouteCardState();
}

class _RouteCardState extends State<RouteCard> {
  bool walkShowing = true;

  @override
  Widget build(BuildContext context) {
    final bool walkObscured =
        widget.routeInfo.timeLeft < widget.routeInfo.walk.time;
    final bool carObscured =
        widget.routeInfo.timeLeft < widget.routeInfo.car.time;
    return Container(
      padding: const EdgeInsets.all(10),
      child: Row(
        children: [
          IconButton(
              onPressed: () {
                setState(() {
                  walkShowing = !walkShowing;
                });
              },
              icon: Icon(
                  walkShowing
                      ? Icons.directions_walk_rounded
                      : Icons.directions_car_rounded,
                  size: 25,
                  color: walkShowing
                      ? walkObscured
                          ? Colors.grey
                          : Colors.blue
                      : carObscured
                          ? Colors.grey
                          : Colors.blue)),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                walkShowing
                    ? doubleTimeToString(widget.routeInfo.walk.time)
                    : doubleTimeToString(widget.routeInfo.car.time),
                style: Theme.of(context).textTheme.labelLarge,
              ),
              Text(
                walkShowing
                    ? doubleLengthToString(widget.routeInfo.walk.length)
                    : doubleLengthToString(widget.routeInfo.car.length),
                style: Theme.of(context).textTheme.labelMedium,
              )
            ],
          ),
          Expanded(
              child: Center(
            child: Container(
              child: walkShowing
                  ? walkObscured
                      ? const Text('Не успеваете!',
                          style: TextStyle(
                              color: Colors.red, fontWeight: FontWeight.w500))
                      : null
                  : carObscured
                      ? const Text('Не успеваете!',
                          style: TextStyle(
                              color: Colors.red, fontWeight: FontWeight.w500))
                      : null,
            ),
          )),
          ElevatedButton(
            onPressed: widget.onRouteButtonTap,
            style: ElevatedButton.styleFrom(
              shape: RoundedRectangleBorder(
                  side: const BorderSide(color: Colors.grey),
                  borderRadius: BorderRadius.circular(12)),
              backgroundColor: Colors.white,
              foregroundColor: Colors.blue,
            ),
            child: const Text(
              'Маршрут',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w500,
              ),
            ),
          )
        ],
      ),
    );
  }
}
