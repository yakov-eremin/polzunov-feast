import 'package:flutter/material.dart';

class OtpRequestCodeButton extends StatelessWidget {
  const OtpRequestCodeButton({
    super.key,
    required this.secondsLeft,
    this.onPressed,
  });

  final int secondsLeft;
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return TextButton(
        onPressed: onPressed,
        child: secondsLeft == 0
            ? RichText(
                text: const TextSpan(
                style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                    color: Colors.blue),
                text: 'Отправить код ещё раз',
              ))
            : RichText(
                text: TextSpan(
                style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w700,
                    color: Colors.grey),
                text: 'Отправить код ещё раз (${secondsLeft.toString()})',
              )));
  }
}
