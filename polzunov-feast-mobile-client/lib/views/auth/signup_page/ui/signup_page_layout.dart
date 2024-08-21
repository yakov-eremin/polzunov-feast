import 'package:feast_mobile/views/auth/signup_page/widgets/signup_goto_signin_button.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/otp_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../widgets/auth_email_input.dart';
import '../../widgets/auth_name_input.dart';
import '../../widgets/auth_password_input.dart';
import '../../widgets/auth_sumbit_button.dart';

class SignupPageLayout extends StatelessWidget {
  const SignupPageLayout({super.key});

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthVM>();
    final otpVM = context.watch<OtpVM>();
    return PopScope(
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: Colors.white,
        appBar: AppBar(
          scrolledUnderElevation: 0,
          backgroundColor: Colors.white,
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () {
              authVM.clearFields();
              goRouter.go('/profile');
            },
          ),
        ),
        body: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
            child: Column(
              children: <Widget>[
                Image.asset('assets/png/house_gray.png'),
                const SizedBox(height: 30),
                Text(
                  'Регистрация',
                  style: Theme.of(context).textTheme.titleLarge,
                ),
                const SizedBox(height: 20),
                AuthNameInput(
                  initialValue: authVM.user.name,
                  errorText: authVM.nameError,
                  onChanged: (newVal) {
                    authVM.nameChanged(newVal);
                  },
                  onFieldSubmitted: (newVal) {
                    authVM.nameChanged(newVal);
                  },
                  onFocusChange: (hasFocus) {
                    if (!hasFocus) authVM.nameChanged(null);
                  },
                ),
                const SizedBox(height: 30),
                AuthEmailInput(
                  initialValue: authVM.user.email,
                  errorText: authVM.emailError,
                  onChanged: (newVal) {
                    authVM.emailChanged(newVal);
                  },
                  onFieldSubmitted: (newVal) {
                    authVM.emailChanged(newVal);
                  },
                  onFocusChange: (hasFocus) {
                    if (!hasFocus) authVM.emailChanged(null);
                  },
                ),
                const SizedBox(height: 30),
                AuthPasswordInput(
                  initialValue: authVM.user.password,
                  passwordObscured: authVM.passwordObscured,
                  errorText: authVM.passwordError,
                  onChanged: (newVal) {
                    authVM.passwordChanged(newVal);
                  },
                  onFieldSubmitted: (newVal) {
                    authVM.passwordChanged(newVal);
                  },
                  onFocusChange: (hasFocus) {
                    if (!hasFocus) authVM.passwordChanged(null);
                  },
                  onPasswordVisibilityChanged: () {
                    authVM.passwordVisibilityChanged();
                  },
                ),
                const SizedBox(height: 30),
                AuthSubmitButton(
                    label: 'Зарегистрироваться',
                    isEnabled: authVM.canContinue,
                    onPressed: () async {
                      if (authVM.canContinue) {
                        if (await authVM.usercheck()) {
                          otpVM.resetData();
                          otpVM.getUser(authVM.user);
                          otpVM.requestNewCode();
                          goRouter.go('/profile/signup/otp');
                        }
                      }
                    }),
                const SizedBox(height: 10),
                SignupGoToSigninButton(
                  onPressed: () {
                    authVM.clearFields();
                    authVM.authMode = AuthMode.signin;
                    goRouter.go('/profile/signin');
                  },
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
