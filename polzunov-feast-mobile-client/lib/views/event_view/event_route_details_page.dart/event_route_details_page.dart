import 'package:feast_mobile/models/route_info.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'widgets/export.dart';

class EventRouteDetailsPage extends StatelessWidget {
  const EventRouteDetailsPage({super.key});

  @override
  Widget build(BuildContext context) {
    RoutingVM routingVM = context.watch<RoutingVM>();
    EventVM eventVM = context.watch<EventVM>();
    AuthVM authVM = context.watch<AuthVM>();

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
      body:
          Column(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
        Flexible(
          flex: 15,
          child: Map(
            routeInfo: eventVM.routeInfo,
            walkRouteShowing: eventVM.walkRouteShowing,
          ),
        ),
        Flexible(
            flex: 3,
            child: Container(
              padding: const EdgeInsets.all(10),
              color: Colors.white,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    eventVM.lastRouteEvent != null
                        ? eventVM.lastRouteEvent!.place.address
                        : "Текущее местоположение",
                    style: Theme.of(context).textTheme.labelLarge,
                  ),
                  const Icon(Icons.arrow_downward_rounded),
                  Text(
                    eventVM.selectedEvent.place.address,
                    style: Theme.of(context).textTheme.labelLarge,
                  )
                ],
              ),
            )),
        const Divider(),
        Flexible(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w500,
                  color: eventVM.walkRouteShowing
                      ? eventVM.routeInfo.haveTimeForWalk
                          ? Colors.black
                          : Colors.red
                      : eventVM.routeInfo.haveTimeForAuto
                          ? Colors.black
                          : Colors.red,
                ),
                eventVM.walkRouteShowing
                    ? eventVM.routeInfo.haveTimeForWalk
                        ? "Успеваем (${doubleTimeToString(eventVM.routeInfo.walk.time)} / ${doubleTimeToString(eventVM.routeInfo.timeLeft)})"
                        : "Не успеваем (${doubleTimeToString(eventVM.routeInfo.walk.time)} / ${doubleTimeToString(eventVM.routeInfo.timeLeft)})"
                    : eventVM.routeInfo.haveTimeForAuto
                        ? "Успеваем (${doubleTimeToString(eventVM.routeInfo.car.time)} / ${doubleTimeToString(eventVM.routeInfo.timeLeft)})"
                        : "Не успеваем (${doubleTimeToString(eventVM.routeInfo.car.time)} / ${doubleTimeToString(eventVM.routeInfo.timeLeft)})",
              ),
            ],
          ),
        ),
        const Divider(),
        Flexible(
            flex: 3,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                RouteSwitch(
                    selected: eventVM.walkRouteShowing,
                    iconData: Icons.directions_walk_rounded,
                    caption: 'Пешком',
                    onTap: () {
                      eventVM.changeRouteShowing();
                    }),
                RouteSwitch(
                    selected: !eventVM.walkRouteShowing,
                    iconData: Icons.directions_car,
                    caption: 'Авто',
                    onTap: () {
                      eventVM.changeRouteShowing();
                    }),
              ],
            )),
        Flexible(
            flex: 2,
            child: AddRouteButton(
              enabled: authVM.logedIn &&
                  (eventVM.routeInfo.haveTimeForWalk ||
                      eventVM.routeInfo.haveTimeForAuto),
              onPressed: () async {
                if (authVM.logedIn) {
                  if (eventVM.routeInfo.haveTimeForWalk ||
                      eventVM.routeInfo.haveTimeForAuto) {
                    switch (await routingVM.addEventToRoutes(
                        authVM.user.accessToken, eventVM.selectedEvent)) {
                      case OperationStatus.success:
                        eventVM.getLastRouteEvent(eventVM.selectedEvent);
                        goRouter.go('/routes');
                        break;
                      case OperationStatus.internalError:
                        showInternalErrorDialog(context);
                        break;
                      case OperationStatus.tokenError:
                        showTokenErrorDialog(context, () {
                          eventVM.getLastRouteEvent(null);
                          goRouter.pop();
                          authVM.setLogedIn(false);
                          goRouter.go('/profile');
                        });
                        break;
                    }
                  } else {
                    showCantReachDialog(context);
                  }
                } else {
                  showUnAuthDialog(context);
                }
              },
            ))
      ]),
    );
  }

  Future<dynamic> showTokenErrorDialog(
      BuildContext context, void Function()? onPressed) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            backgroundColor: Colors.white,
            title: const Text('Ошибка!'),
            titleTextStyle: Theme.of(context).textTheme.titleMedium,
            content: const Text('Вам потребуется повторная авторизация'),
            contentTextStyle: Theme.of(context).textTheme.labelMedium,
            actionsAlignment: MainAxisAlignment.center,
            actions: [
              ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey.shade100,
                      foregroundColor: Colors.red.shade800),
                  onPressed: onPressed,
                  child: const Text('Хорошо')),
            ],
          );
        });
  }

  Future<dynamic> showInternalErrorDialog(BuildContext context) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            backgroundColor: Colors.white,
            title: const Text('Ошибка!'),
            titleTextStyle: Theme.of(context).textTheme.titleMedium,
            content: const Text(
                'Не получилось добавить мероприятие. Проверьте качество связи'),
            contentTextStyle: Theme.of(context).textTheme.labelMedium,
            actionsAlignment: MainAxisAlignment.center,
            actions: [
              ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey.shade100,
                      foregroundColor: Colors.red.shade800),
                  onPressed: () {
                    goRouter.pop();
                  },
                  child: const Text('Хорошо')),
            ],
          );
        });
  }

  Future<dynamic> showCantReachDialog(BuildContext context) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            backgroundColor: Colors.white,
            title: const Text('Мероприятие нельзя добавить'),
            titleTextStyle: Theme.of(context).textTheme.titleMedium,
            content: const Text('Вы не успеваете на него!'),
            contentTextStyle: Theme.of(context).textTheme.labelMedium,
            actionsAlignment: MainAxisAlignment.spaceEvenly,
            actions: [
              ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey.shade100,
                      foregroundColor: Colors.blue.shade800),
                  onPressed: () {
                    goRouter.pop();
                  },
                  child: const Text('Хорошо')),
            ],
          );
        });
  }

  Future<dynamic> showUnAuthDialog(BuildContext context) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            backgroundColor: Colors.white,
            title: const Text('Требуется регистрация'),
            titleTextStyle: Theme.of(context).textTheme.titleMedium,
            content: const Text('Перейти на страницу регистрации?'),
            contentTextStyle: Theme.of(context).textTheme.labelMedium,
            actionsAlignment: MainAxisAlignment.spaceEvenly,
            actions: [
              ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey.shade100,
                      foregroundColor: Colors.blue.shade800),
                  onPressed: () {
                    goRouter.pop();
                  },
                  child: const Text('Позже')),
              ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey.shade100,
                      foregroundColor: Colors.blue.shade800),
                  onPressed: () {
                    goRouter.pop();
                    goRouter.go('/profile');
                  },
                  child: const Text('Перейти')),
            ],
          );
        });
  }
}
