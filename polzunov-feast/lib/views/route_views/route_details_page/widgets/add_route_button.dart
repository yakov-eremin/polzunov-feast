import 'package:flutter/material.dart';

class RemoveRouteButton extends StatelessWidget {
  const RemoveRouteButton({
    super.key,
    required this.enabled,
    this.onPressed,
  });

  final bool enabled;
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(15, 0, 15, 10),
      child: ElevatedButton.icon(
        icon: enabled
            ? const Icon(
                Icons.cancel,
                size: 15,
              )
            : null,
        style: ElevatedButton.styleFrom(
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          backgroundColor: enabled ? Colors.red : Colors.grey,
          foregroundColor: enabled ? Colors.white : Colors.black,
          textStyle: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500),
          minimumSize: const Size.fromHeight(50),
        ),
        onPressed: onPressed,
        label: const Text('Удалить из списка'),
      ),
    );
  }
}
