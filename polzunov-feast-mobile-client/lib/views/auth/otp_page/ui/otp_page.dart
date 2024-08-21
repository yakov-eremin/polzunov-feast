import 'package:feast_mobile/views/auth/otp_page/ui/otp_page_layout.dart';
import 'package:feast_mobile/views/auth/widgets/auth_alert_dialog.dart';
import 'package:feast_mobile/view_models/otp_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class OTPPage extends StatelessWidget {
  const OTPPage({super.key});

  @override
  Widget build(BuildContext context) {
    final otpVM = context.watch<OtpVM>();
    return otpVM.loading
        ? Stack(
            children: [
              const AbsorbPointer(child: OtpPageLayout()),
              Container(
                width: double.infinity,
                height: double.infinity,
                color: Colors.grey[200]!.withOpacity(0.6),
              ),
              const Center(
                child: CircularProgressIndicator(
                  color: Colors.blue,
                  strokeWidth: 5.0,
                ),
              )
            ],
          )
        : otpVM.errorMessage == null
            ? const OtpPageLayout()
            : Stack(children: [
                const AbsorbPointer(child: OTPPage()),
                Container(
                  width: double.infinity,
                  height: double.infinity,
                  color: Colors.grey[200]!.withOpacity(0.6),
                ),
                AuthAlertDialog(
                  title: otpVM.errorMessage!.title,
                  description: otpVM.errorMessage!.description,
                  onSubmit: () {
                    otpVM.setErrorMessage(null);
                  },
                ),
              ]);
  }
}
