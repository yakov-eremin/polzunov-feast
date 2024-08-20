import 'package:feast_mobile/views/auth/signup_page/ui/signup_page_layout.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../widgets/auth_alert_dialog.dart';

class SignupPage extends StatelessWidget {
  const SignupPage({super.key});

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthVM>();
    return authVM.loading
        ? Stack(
            children: [
              const AbsorbPointer(child: SignupPageLayout()),
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
        : authVM.errorMessage == null
            ? const SignupPageLayout()
            : Stack(
                children: [
                  const AbsorbPointer(child: SignupPageLayout()),
                  Container(
                    width: double.infinity,
                    height: double.infinity,
                    color: Colors.grey[200]!.withOpacity(0.6),
                  ),
                  AuthAlertDialog(
                    title: authVM.errorMessage!.title,
                    description: authVM.errorMessage!.description,
                    onSubmit: () {
                      authVM.setErrorMessage(null);
                    },
                  )
                ],
              );
  }
}
