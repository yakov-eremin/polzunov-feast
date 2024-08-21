import 'package:flutter/material.dart';

class ExitAlertDialog extends StatelessWidget {
  const ExitAlertDialog({
    super.key,
    required this.title,
    required this.description,
    this.onCancel,
    this.onSubmit,
  });

  final String title;
  final String description;
  final Function()? onCancel;
  final Function()? onSubmit;

  @override
  Widget build(BuildContext context) {
    return AlertDialog.adaptive(
      backgroundColor: const Color.fromARGB(254, 238, 248, 255),
      title: Text(title),
      titleTextStyle: const TextStyle(
        fontSize: 18,
        fontWeight: FontWeight.w500,
        color: Colors.black,
      ),
      content: Text(description),
      contentTextStyle: const TextStyle(
        fontSize: 14,
        fontWeight: FontWeight.w400,
        color: Colors.black,
      ),
      actions: [
        TextButton(
            onPressed: onSubmit,
            child: const Text(
              "Да",
              style: TextStyle(
                  color: Colors.blue,
                  fontSize: 14,
                  fontWeight: FontWeight.w500),
            )),
        TextButton(
            onPressed: onCancel,
            child: const Text(
              "Нет",
              style: TextStyle(
                  color: Colors.blue,
                  fontSize: 14,
                  fontWeight: FontWeight.w500),
            )),
      ],
      actionsAlignment: MainAxisAlignment.center,
    );
  }
}
