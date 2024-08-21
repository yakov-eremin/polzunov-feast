import 'package:mailer/mailer.dart';
import 'package:mailer/smtp_server.dart';

abstract class EmailService {
  static Future<void> sendEmail({required String to, required String otp}) async {
    final smtpServer = SmtpServer(
      'smtp.yandex.ru',
      username: 'polzunov.feast@yandex.ru',
      password: 'uwbrqirwymblzbgx',
      //port: 25,
    );

    final message = Message()
      ..from = const Address('polzunov.feast@yandex.ru', 'Feast Team')
      ..recipients.add(to)
      ..subject = 'Подтверждение регистрации в Polzunov Feast.'
      ..text = 'Ваш код: \n$otp\n\nСпасибо, что выбрали Polzunov Feast!';

    await send(message, smtpServer);
  }
}
