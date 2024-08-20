import 'package:flutter/material.dart';

class CategoryGridItem extends StatelessWidget {
  const CategoryGridItem({
    super.key,
    required this.active,
    required this.text,
    this.onTap,
  });

  final String text;
  final bool active;
  final void Function()? onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 50,
        decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(15),
            border: Border.all(
                width: 2, color: active ? Colors.blue : Colors.grey.shade300)),
        child: Center(
          child: Text(
            text,
            overflow: TextOverflow.clip,
            softWrap: true,
            style: Theme.of(context).textTheme.labelMedium,
          ),
        ),
      ),
    );
  }
}
