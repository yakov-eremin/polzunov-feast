import 'package:flutter/material.dart';

class Opportunity extends StatelessWidget {
  const Opportunity({
    super.key,
    required this.iconBackgroundColor,
    required this.iconForegroundColor,
    required this.icon,
    required this.title,
    required this.description,
  });

  final Color iconBackgroundColor;
  final Color iconForegroundColor;
  final IconData icon;
  final String title;
  final String description;

  @override
  Widget build(BuildContext context) {
    return Row(children: [
      Container(
          decoration: BoxDecoration(
            color: iconBackgroundColor,
            borderRadius: BorderRadius.circular(40),
          ),
          child: Padding(
            padding: const EdgeInsets.all(7.5),
            child: Icon(
              icon,
              size: 30,
              color: iconForegroundColor,
            ),
          )),
      const SizedBox(width: 15),
      Flexible(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(title, style: Theme.of(context).textTheme.labelLarge),
            Text(
              description,
              softWrap: true,
              maxLines: 2,
            ),
          ],
        ),
      ),
    ]);
  }
}
