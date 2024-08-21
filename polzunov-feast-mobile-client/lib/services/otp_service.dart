import 'package:otp/otp.dart';

class OTPService {
  static const int otpRequestCooldown = 30;
  static const int otpValidTimeInterval = 120;
  late String _validOTP;
  late int _allowRequestTime;

  String generateOTP() {
    _validOTP = OTP.generateTOTPCodeString(
        OTP.randomSecret(), DateTime.now().microsecondsSinceEpoch,
        length: 4, interval: otpRequestCooldown);

    _allowRequestTime = DateTime.fromMicrosecondsSinceEpoch(OTP.lastUsedTime)
        .add(const Duration(seconds: otpRequestCooldown))
        .microsecondsSinceEpoch;

    return _validOTP;
  }

  bool canGenerateOTP() {
    if (OTP.lastUsedTime == 0) {
      return true;
    } else {
      if (OTP.lastUsedTime.compareTo(_allowRequestTime) > 0) {
        return true;
      } else {
        return false;
      }
    }
  }

  OtpVerificationStatus verifyOTP(String otp) {
    // Если код ещё не генерировали, то проверять нечего.
    if (OTP.lastUsedTime == 0) return OtpVerificationStatus.valid;

    // Истек срок валидности
    if ((DateTime.now().microsecondsSinceEpoch - OTP.lastUsedTime) >
        otpValidTimeInterval * 1000000) {
      return OtpVerificationStatus.validTimesUp;
    }

    // Неверный код
    if (_validOTP.compareTo(otp) != 0) return OtpVerificationStatus.invalid;

    return OtpVerificationStatus.valid;
  }
}

enum OtpVerificationStatus { valid, validTimesUp, invalid }
