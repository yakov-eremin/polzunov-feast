import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/views/event_view/event_list_page/widgets/event_card.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/models/event.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

import 'empty_list_placeholder.dart';

class EventsList extends StatelessWidget {
  const EventsList({super.key});

  @override
  Widget build(BuildContext context) {
    final eventVM = context.watch<EventVM>();
    final eventListLength = eventVM.eventList.length;
    if (eventVM.eventLoading) {
      return const Center(
        child: CircularProgressIndicator(
          color: Colors.blue,
        ),
      );
    } else if (eventVM.eventLoadingError) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
              child: Text(
            'Ошибка загрузки мероприятий',
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
                  eventVM.getEvents();
                },
                child: const Text(
                  'Попробовать снова',
                  style: TextStyle(fontSize: 14),
                )),
          )
        ],
      );
    } else if (eventListLength == 0) {
      return const EmptyListPlaceholder();
    } else {
      return ListView.builder(
        controller: eventVM.scrollController,
        shrinkWrap: true,
        padding: const EdgeInsets.all(10),
        itemCount: eventListLength,
        itemBuilder: (context, index) {
          final Event event = eventVM.eventList[index];
          return Column(
            children: [
              EventCard(
                event: event,
                onTap: () async {
                  try {
                    await eventVM.setSelectedEvent(event);
                    context.push('/event_details');
                  } catch (_) {
                    showAccessDeniedDialog(context, () {
                      goRouter.pop();
                    });
                  }
                },
                onButtonPressed: () async {
                  try {
                    await eventVM.setSelectedEvent(event);
                    context.push('/event_route');
                  } catch (_) {
                    showAccessDeniedDialog(context, () {
                      goRouter.pop();
                    });
                  }
                },
              ),
              const Divider(color: Colors.white)
            ],
          );
        },
      );
    }
  }
}

Future<dynamic> showAccessDeniedDialog(
    BuildContext context, void Function()? onPressed) {
  return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          backgroundColor: Colors.white,
          title: const Text('Ошибка!'),
          titleTextStyle: Theme.of(context).textTheme.titleMedium,
          content: const Text(
              'Нельзя построить маршрут от текущего местоположения, не выдав разрешение сервису геолокации!'),
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
