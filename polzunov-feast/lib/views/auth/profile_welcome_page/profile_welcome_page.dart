import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'widgets/opportunity.dart';
import 'widgets/sign_up_button.dart';

class ProfileWelcomePage extends StatelessWidget {
  const ProfileWelcomePage({super.key});

  @override
  Widget build(BuildContext context) {
    final authVM = context.watch<AuthVM>();
    return SafeArea(
        child: Padding(
      padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
      child: CustomScrollView(
        physics: const NeverScrollableScrollPhysics(),
        slivers: [
          SliverFillRemaining(
            hasScrollBody: false,
            child: Column(children: [
              Image.asset("assets/png/croud_grey.png"),
              const SizedBox(height: 20),
              Text(
                'Регистрируйтесь и получайте больше возможностей',
                maxLines: 2,
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 20),
              const Opportunity(
                title: 'Составляйте свой маршрут',
                description:
                    'Подбирайте удоные мероприятия для себя и сохраняйте их в список',
                icon: Icons.format_list_bulleted_add,
                iconBackgroundColor: Colors.amber,
                iconForegroundColor: Colors.white,
              ),
              const SizedBox(height: 20),
              Opportunity(
                title: 'Синхронизация',
                description:
                    'Всё что вы сохранили в приложении, автоматически отправляется на сервер',
                icon: Icons.save,
                iconBackgroundColor: Colors.teal.shade100,
                iconForegroundColor: Colors.white,
              ),
              Expanded(child: Container()),
              SignUpButton(
                onPressed: () {
                  authVM.authMode = AuthMode.signup;
                  goRouter.go('/profile/signup');
                },
              ),
              TextButton(
                  onPressed: () {
                    authVM.authMode = AuthMode.signin;
                    goRouter.go('/profile/signin');
                  },
                  child: const Text(
                    'У меня уже есть аккаунт',
                    style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                        color: Colors.blue),
                  )),
            ]),
          )
        ],
      ),
    ));
  }
}
