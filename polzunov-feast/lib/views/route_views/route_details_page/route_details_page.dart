import 'package:feast_mobile/models/route_info.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'widgets/export.dart';

class RouteDetailsPage extends StatelessWidget {
  const RouteDetailsPage({super.key});

  @override
  Widget build(BuildContext context) {
    RoutingVM routingVM = context.watch<RoutingVM>();
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        shadowColor: Colors.black,
        surfaceTintColor: Colors.white,
        elevation: 10,
        scrolledUnderElevation: 0,
        title: Text(
          'Просмотр маршрутов',
          style: Theme.of(context).textTheme.titleMedium,
        ),
      ),
      body: Column(mainAxisAlignment: MainAxisAlignment.start, children: [
        Flexible(
          fit: FlexFit.tight,
          flex: 18,
          child: Map(
            routeInfo: routingVM.currentRouteInfo,
            walkRouteShowing: routingVM.walkRouteShowing,
          ),
        ),
        const Divider(),
        Flexible(
          flex: 1,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w500,
                  color: routingVM.walkRouteShowing
                      ? routingVM.currentRouteInfo.haveTimeForWalk
                          ? Colors.black
                          : Colors.red
                      : routingVM.currentRouteInfo.haveTimeForAuto
                          ? Colors.black
                          : Colors.red,
                ),
                routingVM.walkRouteShowing
                    ? routingVM.currentRouteInfo.haveTimeForWalk
                        ? "Успеваем (${doubleTimeToString(routingVM.currentRouteInfo.walk.time)} / ${doubleTimeToString(routingVM.currentRouteInfo.timeLeft)})"
                        : "Не успеваем (${doubleTimeToString(routingVM.currentRouteInfo.walk.time)} / ${doubleTimeToString(routingVM.currentRouteInfo.timeLeft)})"
                    : routingVM.currentRouteInfo.haveTimeForAuto
                        ? "Успеваем (${doubleTimeToString(routingVM.currentRouteInfo.car.time)} / ${doubleTimeToString(routingVM.currentRouteInfo.timeLeft)})"
                        : "Не успеваем (${doubleTimeToString(routingVM.currentRouteInfo.car.time)} / ${doubleTimeToString(routingVM.currentRouteInfo.timeLeft)})",
              ),
            ],
          ),
        ),
        const Divider(),
        Flexible(
            fit: FlexFit.tight,
            flex: 2,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                RouteSwitch(
                    selected: routingVM.walkRouteShowing,
                    iconData: Icons.directions_walk_rounded,
                    caption: 'Пешком',
                    onTap: () {
                      routingVM.changeRouteShowing();
                    }),
                RouteSwitch(
                    selected: !routingVM.walkRouteShowing,
                    iconData: Icons.directions_car,
                    caption: 'Авто',
                    onTap: () {
                      routingVM.changeRouteShowing();
                    }),
              ],
            )),
      ]),
    );
  }
}
