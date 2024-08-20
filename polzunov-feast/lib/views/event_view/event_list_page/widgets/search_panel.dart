import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class SearchPanel extends StatelessWidget {
  const SearchPanel({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final eventViewModel = context.watch<EventVM>();
    return Container(
      decoration: const BoxDecoration(boxShadow: [
        BoxShadow(
          color: Colors.grey,
          blurStyle: BlurStyle.outer,
          blurRadius: 20,
          offset: Offset(0, 0),
          spreadRadius: 0.5,
        )
      ], color: Colors.blue),
      height: 80,
      child: Padding(
        padding: const EdgeInsets.fromLTRB(15, 0, 15, 0),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text('Найдены мероприятия:',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w500,
                  color: Colors.white,
                )),
            GestureDetector(
              onTap: () {
                eventViewModel.createFiltersDup();
                goRouter.push('/event_filters');
              },
              child: Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(15)),
                  child: const Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text('Фильтры'),
                      SizedBox(width: 10),
                      Icon(Icons.tune),
                    ],
                  )),
            )
          ],
        ),
      ),
    );
  }
}
