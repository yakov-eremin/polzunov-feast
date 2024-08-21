import 'dart:async';
import 'dart:io';

import 'package:feast_mobile/models/user.dart';
import 'package:feast_mobile/services/email_service.dart';
import 'package:feast_mobile/services/db_service.dart';
import 'package:feast_mobile/services/otp_service.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:flutter/material.dart';

const int otpRequestCooldown = 30;

class OtpVM extends ChangeNotifier {
  bool loading = false;
  OTPService? otpService = OTPService();
  late User user;
  int secondsLeft = 0;
  String? codeError;
  String enteredCode = '';
  bool signUpButtonEnabled = false;
  bool codeSendError = false;
  ErrorMessage? errorMessage;

  StreamSubscription<int>? tickerSubs;

  getUser(User user) {
    this.user = user;
  }

  setErrorMessage(ErrorMessage? msg) {
    errorMessage = msg;
    notifyListeners();
  }

  setLoading(bool loading) {
    this.loading = loading;
    notifyListeners();
  }

  timerTicked(int secondsLeft) {
    this.secondsLeft = secondsLeft;
    notifyListeners();
  }

  setCodeError(String codeError) {
    this.codeError = codeError;
    notifyListeners();
  }

  resetData() {
    loading = false;
    secondsLeft = 0;
    codeError = null;
    enteredCode = '';
    signUpButtonEnabled = false;
    codeSendError = false;
    errorMessage = null;
    otpService = OTPService();
  }

  stopTimer() {
    tickerSubs?.cancel();
  }

  requestNewCode() async {
    final code = otpService?.generateOTP();
    try {
      setLoading(true);
      await EmailService.sendEmail(to: user.email, otp: code!)
          .timeout(const Duration(seconds: 3));
      tickerSubs?.cancel();
      tickerSubs = Stream.periodic(const Duration(seconds: 1),
              (i) => OTPService.otpRequestCooldown - i - 1)
          .take(OTPService.otpRequestCooldown)
          .listen((secondsLeft) => timerTicked(secondsLeft));

      signUpButtonEnabled = false;
      codeSendError = false;
    } catch (_) {
      signUpButtonEnabled = false;
      codeSendError = true;
    } finally {
      setLoading(false);
    }
  }

  Future<bool> verifyCode(String code) async {
    switch (otpService?.verifyOTP(code)) {
      case OtpVerificationStatus.valid:
        {
          try {
            setLoading(true);
            user.accessToken = await DBService.userSignUp(
                user.name, user.email, user.password, user.accessToken);
            setLoading(false);
            return true;
          } on SignUpException catch (e) {
            if (e.type == SignUpFailure.emailAlreadyExists) {
              setErrorMessage(ErrorMessage(
                title: 'Email уже занят',
                description: 'Попробуйте другой email',
              ));
            } else if (e.type == SignUpFailure.phoneAlreadyExists) {
              setErrorMessage(ErrorMessage(
                title: 'Телефон уже занят',
                description: 'Попробуйте другой номер',
              ));
            }
            setLoading(false);
            return false;
          } on TimeoutException catch (_) {
            setErrorMessage(ErrorMessage(
              title: 'Ошибка связи',
              description: 'Слабое интернет-соединение',
            ));
            setLoading(false);
            return false;
          } on SocketException catch (_) {
            setErrorMessage(ErrorMessage(
              title: 'Ошибка связи',
              description: 'Проверьте интернет-соединение',
            ));
            setLoading(false);
            return false;
          } on Exception catch (_) {
            setErrorMessage(ErrorMessage(
              title: 'Неизвестная ошибка',
              description: 'Попробуйте позже',
            ));
            setLoading(false);
            return false;
          }
        }
      case OtpVerificationStatus.validTimesUp:
        setCodeError('Код устарел');
        return false;
      case OtpVerificationStatus.invalid:
        setCodeError('Неверный код');
        return false;
      default:
        setCodeError('Неизвестная ошибка');
        return false;
    }
  }
}
