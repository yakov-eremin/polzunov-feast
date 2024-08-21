import 'package:flutter/material.dart';

class OtpSignUpButton extends StatelessWidget {
  const OtpSignUpButton({super.key, required this.enabled, this.onPressed});

  final bool enabled;
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
        style: ElevatedButton.styleFrom(
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          foregroundColor: enabled ? Colors.white : Colors.grey[700],
          backgroundColor: enabled ? Colors.blue : Colors.grey[400],
          textStyle: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
          minimumSize: const Size.fromHeight(50),
        ),
        onPressed: onPressed,
        child: const Text('Отправить'));
  }
}
