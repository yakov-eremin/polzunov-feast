import 'package:flutter/material.dart';

class AuthSubmitButton extends StatelessWidget {
  const AuthSubmitButton(
      {super.key,
      required this.isEnabled,
      this.onPressed,
      required this.label});

  final bool isEnabled;
  final void Function()? onPressed;
  final String label;

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
        style: ElevatedButton.styleFrom(
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          foregroundColor: isEnabled ? Colors.white : Colors.grey[700],
          backgroundColor: isEnabled ? Colors.blue : Colors.grey[400],
          textStyle: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
          minimumSize: const Size.fromHeight(50),
        ),
        onPressed: onPressed,
        child: Text(label));
  }
}
