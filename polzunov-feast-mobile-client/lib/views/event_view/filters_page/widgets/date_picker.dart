import 'package:flutter/material.dart';

class DatePicker extends StatelessWidget {
  const DatePicker({
    super.key,
    required this.date,
    this.onTap,
  });

  final String date;
  final Function()? onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
          padding: const EdgeInsets.all(10),
          decoration: BoxDecoration(
              color: Colors.white,
              border: Border.all(width: 2, color: Colors.grey.shade300),
              borderRadius: BorderRadius.circular(15)),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            children: [
              Text(date, style: Theme.of(context).textTheme.labelMedium),
              // SizedBox(width: 40),
              const Icon(Icons.arrow_drop_down)
            ],
          )),
    );
  }
}
