import 'package:flutter/material.dart';

class OtpEmailLabel extends StatelessWidget {
  const OtpEmailLabel({super.key, required this.email});

  final String email;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(
          'Мы выслали вам специальный код',
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w400,
            color: Colors.grey[700],
          ),
        ),
        RichText(
          text: TextSpan(children: <InlineSpan>[
            TextSpan(
              style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w400,
                  color: Colors.grey[700]),
              text: 'на',
            ),
            const WidgetSpan(child: SizedBox(width: 5)),
            TextSpan(
              style: const TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w700,
                  color: Colors.blue),
              text: email,
            ),
          ]),
        ),
        Text(
          'Если код не пришёл, проверьте раздел Спам',
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w400,
            color: Colors.grey[600],
          ),
        )
      ],
    );
  }
}
