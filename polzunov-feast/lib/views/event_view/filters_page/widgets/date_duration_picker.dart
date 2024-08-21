import 'package:flutter/material.dart';

class DateDurationPicker extends StatelessWidget {
  const DateDurationPicker({
    super.key,
    this.onTap,
    required this.startDate,
    required this.endDate,
  });

  final String startDate;
  final String endDate;
  final Function()? onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
          decoration: BoxDecoration(
              color: Colors.white,
              border: Border.all(width: 2, color: Colors.grey.shade300),
              borderRadius: BorderRadius.circular(15)),
          child: Padding(
            padding: const EdgeInsets.all(10),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('$startDate — $endDate',
                    style: Theme.of(context).textTheme.labelMedium),
                const Icon(Icons.arrow_drop_down)
              ],
            ),
          )),
    );
  }
}
