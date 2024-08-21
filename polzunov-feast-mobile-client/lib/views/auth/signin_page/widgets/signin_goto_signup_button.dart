import 'package:flutter/material.dart';

class SigninGoToSignupButton extends StatelessWidget {
  const SigninGoToSignupButton({
    super.key,
    this.onPressed,
  });

  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return TextButton(
        onPressed: onPressed,
        child: RichText(
          text: TextSpan(children: <InlineSpan>[
            TextSpan(
              text: 'Нет аккаунта?',
              style: Theme.of(context).textTheme.labelLarge,
            ),
            const WidgetSpan(child: SizedBox(width: 10)),
            const TextSpan(
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w500,
                color: Colors.blue,
              ),
              text: 'Зарегистрироваться',
            ),
          ]),
        ));
  }
}
