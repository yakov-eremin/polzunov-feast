import 'package:easy_image_viewer/easy_image_viewer.dart';
import 'package:feast_mobile/models/event.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/models/category.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

class EventDetailsPage extends StatelessWidget {
  const EventDetailsPage({super.key});

  @override
  Widget build(BuildContext context) {
    final selectedEvent = context.watch<EventVM>().selectedEvent;

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            context.pop();
          },
        ),
        backgroundColor: Colors.white,
        title: const Text('О мероприятии'),
        titleTextStyle: const TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.w500,
          color: Colors.black,
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              selectedEvent.name,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w500,
                color: Colors.black,
              ),
            ),
            if (selectedEvent.description != '') const SizedBox(height: 10),
            if (selectedEvent.description != '')
              RichText(
                maxLines: 4,
                text: TextSpan(text: '', children: [
                  TextSpan(
                    text: selectedEvent.description,
                    style: const TextStyle(color: Colors.black),
                    recognizer: TapGestureRecognizer()..onTap = () {},
                  )
                ]),
              ),
            Column(
              children: [
                const SizedBox(height: 20),
                Row(
                  children: [
                    Icon(
                      Icons.access_time_rounded,
                      color: Colors.grey[700],
                    ),
                    const SizedBox(width: 10),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Время:'),
                        Text(dateFormatter(selectedEvent.timeRange))
                      ],
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Icon(Icons.place, color: Colors.grey[700]),
                    const SizedBox(width: 10),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Адрес:'),
                        Text(selectedEvent.place.address)
                      ],
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Icon(Icons.tune, color: Colors.grey[700]),
                    const SizedBox(width: 10),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Категории:'),
                        Text(
                            selectedEvent.categories.map((CategoryModel e) => e.name.toString()).toList().join(' | '))
                      ],
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Icon(Icons.align_horizontal_left_sharp,
                        color: Colors.grey[700]),
                    const SizedBox(width: 10),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Возрастное ограничение:'),
                        Text(
                          '${selectedEvent.ageLimit}+',
                        )
                      ],
                    ),
                  ],
                ),
              ],
            ),
            if (selectedEvent.imageUrls.isNotEmpty) const SizedBox(height: 20),
            if (selectedEvent.imageUrls.isNotEmpty)
              const Text(
                'Фотографии',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w500,
                  color: Colors.black,
                ),
              ),
            Expanded(
              child: GridView.extent(
                  maxCrossAxisExtent: 150,
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                  children: selectedEvent.imageUrls
                      .map((e) => GestureDetector(
                            onTap: () {
                              showImageViewer(
                                backgroundColor: Colors.white,
                                closeButtonColor: Colors.black,
                                context,
                                Image.network(e, fit: BoxFit.scaleDown).image,
                                swipeDismissible: false,
                              );
                            },
                            child: Container(
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(15),
                              ),
                              child: Image.network(e, fit: BoxFit.scaleDown),
                            ),
                          ))
                      .toList()),
            ),
            ElevatedButton.icon(
              icon: const Icon(
                Icons.check,
                size: 20,
              ),
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12)),
                backgroundColor: Colors.blue,
                foregroundColor: Colors.white,
                textStyle:
                    const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
                minimumSize: const Size.fromHeight(50),
              ),
              onPressed: () {
                goRouter.push('/event_route');
              },
              label: const Text('Построить маршрут'),
            )
          ],
        ),
      ),
    );
  }
}
