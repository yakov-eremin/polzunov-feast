import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'widgets/events_list.dart';
import 'widgets/filters_button.dart';

class EventListPage extends StatelessWidget {
  const EventListPage({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final EventVM eventVM = context.watch<EventVM>();
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        scrolledUnderElevation: 0,
        backgroundColor: Colors.blue,
        shadowColor: Colors.black,
        surfaceTintColor: Colors.blue,
        elevation: 3,
        title: const Text('Найдены мероприятия:',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.w500,
              color: Colors.white,
            )),
        actions: [
          FiltersButton(
            onTap: () {
              eventVM.createFiltersDup();
              goRouter.push('/event_filters');
            },
          )
        ],
      ),
      body: const SafeArea(child: EventsList()),
    );
  }
}
