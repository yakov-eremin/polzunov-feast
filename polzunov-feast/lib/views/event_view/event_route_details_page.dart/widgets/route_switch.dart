import 'package:flutter/material.dart';

class RouteSwitch extends StatelessWidget {
  const RouteSwitch({
    super.key,
    required this.iconData,
    required this.caption,
    required this.selected,
    this.onTap,
  });

  final IconData iconData;
  final String caption;
  final bool selected;
  final void Function()? onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(15),
            color: selected ? Colors.blue.withOpacity(0.1) : Colors.white,
            border: Border.all(
              color: selected ? Colors.blue : Colors.grey.shade400,
              width: 1,
            )),
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Row(
            children: [
              Icon(
                iconData,
                color: selected ? Colors.blue.shade800 : Colors.grey.shade700,
                size: 20,
              ),
              const SizedBox(width: 10),
              Text(caption)
            ],
          ),
        ),
      ),
    );
  }
}
