import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../widgets/auth_email_input.dart';
import '../../widgets/auth_password_input.dart';
import '../../widgets/auth_sumbit_button.dart';
import '../widgets/signin_goto_signup_button.dart';

class SigninPageLayout extends StatelessWidget {
  const SigninPageLayout({super.key});

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthVM>();
    final routingVM = context.watch<RoutingVM>();
    final eventVM = context.watch<EventVM>();
    return PopScope(
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: Colors.white,
        appBar: AppBar(
          backgroundColor: Colors.white,
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () {
              authVM.clearFields();
              goRouter.go('/profile');
            },
          ),
        ),
        body: CustomScrollView(slivers: [
          SliverFillRemaining(
            hasScrollBody: false,
            child: Padding(
              padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
              child: Column(
                children: <Widget>[
                  Image.asset('assets/png/house_gray.png'),
                  const SizedBox(height: 30),
                  Text(
                    'Авторизация',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 20),
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
                  Expanded(child: Container()),
                  AuthSubmitButton(
                    label: 'Войти',
                    isEnabled: authVM.canContinue,
                    onPressed: () async {
                      if (authVM.canContinue) {
                        if (await authVM.signin()) {
                          authVM.setLogedIn(true);
                          await routingVM
                              .getRouteEvents(authVM.user.accessToken);
                          eventVM.getLastRouteEvent(
                              routingVM.routeEvents.isNotEmpty
                                  ? routingVM.routeEvents.last
                                  : null);
                          goRouter.go('/profile/signin/success');
                        }
                      }
                    },
                  ),
                  const SizedBox(height: 10),
                  SigninGoToSignupButton(
                    onPressed: () {
                      authVM.clearFields();
                      authVM.authMode = AuthMode.signup;
                      goRouter.go('/profile/signup');
                    },
                  )
                ],
              ),
            ),
          ),
        ]),
      ),
    );
  }
}
