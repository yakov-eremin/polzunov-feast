import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';

import 'widgets/export.dart';

class RouteListPage extends StatelessWidget {
  const RouteListPage({super.key});

  @override
  Widget build(BuildContext context) {
    AuthVM authVM = context.watch<AuthVM>();
    RoutingVM routingVM = context.watch<RoutingVM>();
    EventVM eventVM = context.watch<EventVM>();

    if (!authVM.logedIn) {
      return const RequreLoginPlaceholder();
    } else {
      return Scaffold(
          backgroundColor: Colors.white,
          appBar: AppBar(
            backgroundColor: Colors.blue,
            surfaceTintColor: Colors.blue,
            foregroundColor: Colors.white,
            shadowColor: Colors.black,
            elevation: 3,
            scrolledUnderElevation: 0,
            title: const Text(
              'Мой список',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w500,
                color: Colors.white,
              ),
            ),
          ),
          body: routingVM.routesLoading
              ? const Center(
                  child: CircularProgressIndicator(
                    color: Colors.blue,
                  ),
                )
              : routingVM.routesLoadingError
                  ? Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Center(
                            child: Text(
                          'Ошибка загрузки расписания',
                          style: TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w500,
                            color: Colors.red.shade800,
                          ),
                        )),
                        Center(
                          child: ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.grey.shade400,
                                  foregroundColor: Colors.white),
                              onPressed: () {
                                routingVM
                                    .getRouteEvents(authVM.user.accessToken);
                                eventVM.getLastRouteEvent(
                                    routingVM.routeEvents.isNotEmpty
                                        ? routingVM.routeEvents.last
                                        : null);
                              },
                              child: const Text(
                                'Попробовать снова',
                                style: TextStyle(fontSize: 14),
                              )),
                        )
                      ],
                    )
                  : routingVM.routeEvents.isNotEmpty
                      ? ListView.builder(
                          padding: const EdgeInsets.fromLTRB(0, 10, 0, 0),
                          itemCount: routingVM.routeEvents.length +
                              routingVM.routeInfos.length,
                          itemBuilder: (context, index) {
                            final newIndex = index ~/ 2;
                            if (index.isEven) {
                              return EventCard(
                                startTime:
                                    "${DateFormat.MMMMd('ru').format(routingVM.routeEvents[newIndex].timeRange.start)} ${DateFormat('Hm').format(routingVM.routeEvents[newIndex].timeRange.start)}",
                                endTime:
                                    "${DateFormat.MMMMd('ru').format(routingVM.routeEvents[newIndex].timeRange.end)} ${DateFormat('Hm').format(routingVM.routeEvents[newIndex].timeRange.end)}",
                                address: routingVM
                                    .routeEvents[newIndex].place.address,
                                name: routingVM.routeEvents[newIndex].name,
                                onTap: () async {
                                  await routingVM.setCurrentEvent(newIndex);
                                  goRouter.push('/routes_event_details');
                                },
                              );
                            } else {
                              return RouteCard(
                                routeInfo: routingVM.routeInfos[newIndex],
                                onRouteButtonTap: () async {
                                  await routingVM.setCurrentRoute(newIndex);
                                  goRouter.push('/route_details');
                                },
                              );
                            }
                          },
                        )
                      : const EmptyListPlaceholder());
    }
  }
}
