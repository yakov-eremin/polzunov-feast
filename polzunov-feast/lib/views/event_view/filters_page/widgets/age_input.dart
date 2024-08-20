import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AgeInput extends StatelessWidget {
  const AgeInput({super.key, this.onChanged, required this.controller});

  final Function(String)? onChanged;
  final TextEditingController controller;

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        hintText: 'Введите возраст',
        hintStyle: Theme.of(context).textTheme.labelSmall,
        counterStyle: const TextStyle(fontSize: 0),
        prefixIcon: const Icon(Icons.align_horizontal_left_rounded),
        filled: true,
        fillColor: Colors.white,
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(15),
          borderSide: BorderSide(color: Colors.grey.shade300, width: 2),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(15),
          borderSide: BorderSide(color: Colors.grey.shade300, width: 2),
        ),
      ),
      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
      maxLength: 2,
      keyboardType: TextInputType.number,
      onChanged: onChanged,
    );
  }
}
