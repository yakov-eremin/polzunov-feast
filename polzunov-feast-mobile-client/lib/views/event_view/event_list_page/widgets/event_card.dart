import 'package:feast_mobile/models/event.dart';
import 'package:flutter/material.dart';

class EventCard extends StatelessWidget {
  const EventCard({
    super.key,
    required this.event,
    this.onTap,
    this.onButtonPressed,
  });

  final Event event;
  final void Function()? onTap;
  final void Function()? onButtonPressed;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Card(
        elevation: 5,
        surfaceTintColor: Colors.grey,
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                dateFormatter(event.timeRange),
                style: Theme.of(context).textTheme.labelLarge,
              ),
              const SizedBox(height: 5),
              Text(
                event.name,
                style: Theme.of(context).textTheme.labelMedium,
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  Icon(
                    Icons.place,
                    size: 20,
                    color: Colors.grey[700],
                  ),
                  const SizedBox(width: 10),
                  Text(
                    event.place.address,
                    style: TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w400,
                        color: Colors.grey[600]),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              ElevatedButton.icon(
                icon: const Icon(
                  Icons.check,
                  size: 15,
                ),
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12)),
                  backgroundColor: Colors.blue,
                  foregroundColor: Colors.white,
                  textStyle: const TextStyle(
                      fontSize: 12, fontWeight: FontWeight.w500),
                  minimumSize: const Size.fromHeight(40),
                ),
                onPressed: onButtonPressed,
                label: const Text('Построить маршрут'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
