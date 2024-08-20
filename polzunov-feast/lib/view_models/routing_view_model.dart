import 'package:feast_mobile/models/event.dart';
import 'package:feast_mobile/models/route.dart';
import 'package:feast_mobile/models/route_info.dart';
import 'package:feast_mobile/services/db_service.dart';
import 'package:feast_mobile/services/routing_service.dart';
import 'package:flutter/material.dart';

enum OperationStatus { success, tokenError, internalError }

class RoutingVM extends ChangeNotifier {
  List<Event> routeEvents = [];
  List<RouteInfo> routeInfos = [];
  late RouteInfo currentRouteInfo;
  late Event currentEvent;
  late bool routesLoadingError;
  bool routesLoading = false;
  bool walkRouteShowing = true;

  changeRouteShowing() {
    walkRouteShowing = !walkRouteShowing;
    notifyListeners();
  }

  getRouteEvents(String authToken) async {
    try {
      routesLoadingError = false;
      routesLoading = true;
      routeEvents = await DBService.getRouteEvents(authToken);
      if (routeEvents.length > 1) {
        for (int i = 0; i < routeEvents.length - 1; i++) {
          routeInfos.add(
              await buildRoutesBetween(routeEvents[i], routeEvents[i + 1]));
        }
      }
    } catch (e) {
      routesLoadingError = true;
    } finally {
      routesLoading = false;
    }
  }

  Future<OperationStatus> deleteRouteEvent(
      String accessToken, int index) async {
    final Event temp = routeEvents.elementAt(index);
    try {
      routeEvents.removeAt(index);
      await DBService.putRoute(accessToken, routeEvents);
      if (index == 0) {
        if (routeInfos.isNotEmpty) routeInfos.removeAt(0);
      } else if (index == routeEvents.length) {
        if (routeInfos.isNotEmpty) routeInfos.removeLast();
      } else {
        routeInfos.removeAt(index - 1);
        routeInfos.removeAt(index - 1);
        routeInfos.insert(
            index - 1,
            await buildRoutesBetween(
                routeEvents[index - 1], routeEvents[index]));
      }
      notifyListeners();
      return OperationStatus.success;
    } on TokenAuthFailed catch (_) {
      routeEvents.insert(index, temp);
      return OperationStatus.tokenError;
    } catch (_) {
      routeEvents.insert(index, temp);
      return OperationStatus.internalError;
    }
  }

  Future<OperationStatus> addEventToRoutes(
      String accessToken, Event event) async {
    try {
      routeEvents.add(event);
      await DBService.putRoute(accessToken, routeEvents);
      if (routeEvents.length >= 2) {
        routeInfos.add(await buildRoutesBetween(
            routeEvents[routeEvents.length - 2],
            routeEvents[routeEvents.length - 1]));
      }
      notifyListeners();
      return OperationStatus.success;
    } on TokenAuthFailed catch (_) {
      routeEvents.remove(event);
      return OperationStatus.tokenError;
    } catch (_) {
      routeEvents.remove(event);
      return OperationStatus.internalError;
    }
  }

  Future<RouteInfo> buildRoutesBetween(Event start, Event end) async {
    final RouteModel walkRoute = await RoutingService.getRouteBasicFromAddress(
        start.place.address, end.place.address, RouteType.walk);
    final RouteModel carRoute = await RoutingService.getRouteBasicFromAddress(
        start.place.address, end.place.address, RouteType.car);
    final double timeLeft =
        (((end.timeRange.start.hour * 60) + end.timeRange.start.minute) -
                ((start.timeRange.end.hour * 60) + start.timeRange.end.minute))
            .toDouble();
    return RouteInfo(walk: walkRoute, car: carRoute, timeLeft: timeLeft);
  }

  setCurrentRoute(int index) async {
    final RouteModel walkRoute = await RoutingService.getRouteFullFromAddress(
        routeEvents[index].place.address,
        routeEvents[index + 1].place.address,
        RouteType.walk);
    final RouteModel carRoute = await RoutingService.getRouteFullFromAddress(
        routeEvents[index].place.address,
        routeEvents[index + 1].place.address,
        RouteType.car);
    final double timeLeft =
        (((routeEvents[index + 1].timeRange.start.hour * 60) +
                    routeEvents[index + 1].timeRange.start.minute) -
                ((routeEvents[index].timeRange.end.hour * 60) +
                    routeEvents[index].timeRange.end.minute))
            .toDouble();
    currentRouteInfo =
        RouteInfo(walk: walkRoute, car: carRoute, timeLeft: timeLeft);
    notifyListeners();
  }

  setCurrentEvent(int index) {
    currentEvent = routeEvents[index];
    notifyListeners();
  }
}
