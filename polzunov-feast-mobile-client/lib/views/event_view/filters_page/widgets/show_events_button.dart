import 'package:flutter/material.dart';

class ShowEventsButton extends StatelessWidget {
  const ShowEventsButton({
    super.key,
    this.onPressed,
  });

  final Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      onPressed: onPressed,
      style: ElevatedButton.styleFrom(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        foregroundColor: Colors.white,
        backgroundColor: Colors.blue,
        textStyle: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
        minimumSize: const Size.fromHeight(50),
      ),
      child: const Text('Показать мероприятия'),
    );
  }
}
