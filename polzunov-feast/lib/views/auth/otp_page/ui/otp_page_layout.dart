import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/view_models/otp_view_model.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../widgets/otp_code_input.dart';
import '../widgets/otp_email_label.dart';
import '../widgets/otp_email_label_error.dart';
import '../widgets/otp_request_code_button.dart';
import '../widgets/otp_singup_button.dart';

class OtpPageLayout extends StatelessWidget {
  const OtpPageLayout({super.key});

  @override
  Widget build(BuildContext context) {
    final otpVM = context.watch<OtpVM>();
    final authVM = context.watch<AuthVM>();
    final routingVM = context.watch<RoutingVM>();
    final eventVM = context.watch<EventVM>();
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            otpVM.stopTimer();
            goRouter.go('/profile/signup');
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
                const Text('Введите код из почты ',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w500,
                    )),
                const SizedBox(height: 10),
                otpVM.codeSendError
                    ? OtpEmailLabelError(email: otpVM.user.email)
                    : OtpEmailLabel(email: otpVM.user.email),
                const SizedBox(height: 20),
                OtpCodeInput(
                  enabled: !otpVM.codeSendError,
                  errorText: otpVM.codeError,
                  onCompleted: (p0) async {
                    if (await otpVM.verifyCode(p0)) {
                      authVM.setLogedIn(true);
                      await routingVM.getRouteEvents(authVM.user.accessToken);
                      eventVM.getLastRouteEvent(routingVM.routeEvents.isNotEmpty
                          ? routingVM.routeEvents.last
                          : null);
                      goRouter.go('/profile/signup/otp/success');
                    }
                  },
                ),
                OtpRequestCodeButton(
                  secondsLeft: otpVM.secondsLeft,
                  onPressed: () {
                    if (otpVM.secondsLeft == 0) {
                      otpVM.requestNewCode();
                    }
                  },
                ),
                const SizedBox(height: 50),
                OtpSignUpButton(
                    enabled: otpVM.signUpButtonEnabled,
                    onPressed: () {
                      debugPrint('Конпка нажатоа');
                    })
              ],
            ),
          ),
        ),
      ]),
    );
  }
}
