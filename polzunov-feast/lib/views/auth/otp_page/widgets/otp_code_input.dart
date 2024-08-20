import 'package:flutter/services.dart';
import 'package:flutter/material.dart';
import 'package:pin_code_fields/pin_code_fields.dart';

class OtpCodeInput extends StatelessWidget {
  const OtpCodeInput(
      {super.key,
      this.errorText,
      this.onCompleted,
      this.onChanged,
      required this.enabled});

  final String? errorText;
  final bool enabled;
  final Function(String)? onCompleted;
  final Function(String)? onChanged;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          Text("Код", style: Theme.of(context).textTheme.labelLarge),
          if (errorText != null)
            Text(errorText!,
                style: const TextStyle(
                    color: Colors.red,
                    fontWeight: FontWeight.w500,
                    fontSize: 12))
        ]),
        const SizedBox(height: 5),
        PinCodeTextField(
          enabled: enabled,
          length: 4,
          onCompleted: onCompleted,
          onChanged: onChanged,
          appContext: context,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          animationType: AnimationType.none,
          keyboardType: TextInputType.number,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          errorTextSpace: 0,
          enableActiveFill: true,
          showCursor: false,
          pinTheme: PinTheme(
            shape: PinCodeFieldShape.box,
            borderRadius: BorderRadius.circular(15),
            fieldWidth: 80,
            borderWidth: 0.5,
            activeFillColor: Colors.white,
            selectedFillColor: Colors.white,
            inactiveFillColor: Colors.white,
            disabledColor: Colors.grey[200],
            activeColor: Colors.grey[200],
            selectedColor: Colors.blue,
            inactiveColor: Colors.grey[200],
          ),
        )
      ],
    );
  }
}
