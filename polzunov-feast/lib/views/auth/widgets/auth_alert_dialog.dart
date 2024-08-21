import 'package:flutter/material.dart';

class AuthAlertDialog extends StatelessWidget {
  const AuthAlertDialog({
    super.key,
    required this.title,
    required this.description,
    this.onSubmit,
  });

  final String title;
  final String description;
  final void Function()? onSubmit;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
          decoration: BoxDecoration(
              color: Colors.red.shade100,
              borderRadius: BorderRadius.circular(15)),
          child: Padding(
            padding: const EdgeInsets.all(10.0),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  title,
                  style: Theme.of(context).textTheme.labelLarge,
                ),
                const SizedBox(height: 10),
                Text(
                  description,
                  style: Theme.of(context).textTheme.labelMedium,
                ),
                const SizedBox(height: 10),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      foregroundColor: Colors.blue,
                      backgroundColor: Colors.white),
                  onPressed: onSubmit,
                  child: const Text('Ок'),
                )
              ],
            ),
          )),
    );
  }
}
