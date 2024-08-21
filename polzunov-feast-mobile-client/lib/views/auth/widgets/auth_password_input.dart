import 'package:flutter/material.dart';

class AuthPasswordInput extends StatelessWidget {
  const AuthPasswordInput(
      {super.key,
      this.errorText,
      required this.passwordObscured,
      this.onChanged,
      this.onFocusChange,
      this.onFieldSubmitted,
      this.onPasswordVisibilityChanged,
      required this.initialValue});

  final String? errorText;
  final String initialValue;
  final bool passwordObscured;
  final void Function(String)? onChanged;
  final void Function(bool)? onFocusChange;
  final void Function(String)? onFieldSubmitted;
  final void Function()? onPasswordVisibilityChanged;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          Text("Пароль", style: Theme.of(context).textTheme.labelLarge),
          if (errorText != null)
            Text(errorText!,
                style: const TextStyle(
                  color: Colors.red,
                  fontWeight: FontWeight.w500,
                  fontSize: 12,
                ))
        ]),
        const SizedBox(height: 5),
        Focus(
          onFocusChange: onFocusChange,
          child: TextFormField(
            initialValue: initialValue,
            onChanged: onChanged,
            onFieldSubmitted: onFieldSubmitted,
            keyboardType: TextInputType.emailAddress,
            maxLength: 32,
            obscureText: passwordObscured,
            decoration: InputDecoration(
              counterStyle: const TextStyle(fontSize: 0),
              contentPadding: const EdgeInsets.all(12),
              filled: true,
              fillColor: errorText != null ? Colors.red[100] : Colors.white,
              hintText: "Введите пароль",
              hintStyle: Theme.of(context).textTheme.labelSmall,
              suffixIcon: IconButton(
                icon: passwordObscured
                    ? const Icon(Icons.visibility)
                    : const Icon(Icons.visibility_off),
                onPressed: onPasswordVisibilityChanged,
              ),
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
