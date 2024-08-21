import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AuthNameInput extends StatelessWidget {
  const AuthNameInput(
      {super.key,
      this.errorText,
      required this.initialValue,
      this.onFocusChange,
      this.onFieldSubmitted,
      this.onChanged});

  final String? errorText;
  final String initialValue;
  final void Function(bool)? onFocusChange;
  final void Function(String)? onFieldSubmitted;
  final void Function(String)? onChanged;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          Text("Как к вам обращаться?",
              style: Theme.of(context).textTheme.labelLarge),
          if (errorText != null)
            Text(errorText!,
                style: const TextStyle(
                    color: Colors.red,
                    fontWeight: FontWeight.w500,
                    fontSize: 12))
        ]),
        const SizedBox(height: 5),
        Focus(
          onFocusChange: onFocusChange,
          child: TextFormField(
            initialValue: initialValue,
            onChanged: onChanged,
            onFieldSubmitted: onFieldSubmitted,
            keyboardType: TextInputType.emailAddress,
            inputFormatters: [
              FilteringTextInputFormatter.allow(
                  RegExp(r"[a-zA-Z0-9.a-zA-Z0-9!@#$%&'*+-/=?^_`{|}~]")),
            ],
            maxLength: 256,
            decoration: InputDecoration(
              contentPadding: const EdgeInsets.all(12),
              filled: true,
              fillColor: errorText != null ? Colors.red[100] : Colors.white,
              hintText: "Введите ваше имя",
              hintStyle: Theme.of(context).textTheme.labelSmall,
              counterStyle: const TextStyle(fontSize: 0),
              enabledBorder: OutlineInputBorder(
                borderSide: BorderSide(
                    color: errorText != null ? Colors.red : Colors.black),
                borderRadius: BorderRadius.circular(12),
              ),
              focusedBorder: OutlineInputBorder(
                  borderSide: BorderSide(
                      color: errorText != null ? Colors.red : Colors.blue),
                  borderRadius: BorderRadius.circular(20)),
            ),
          ),
        ),
      ],
    );
  }
}
