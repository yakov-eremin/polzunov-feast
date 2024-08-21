import 'package:feast_mobile/view_models/auth_view_model.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/otp_view_model.dart';
import 'package:feast_mobile/view_models/routing_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'text_theme.dart';

void main() {
  runApp(const FeastMobileApp());
}

class FeastMobileApp extends StatelessWidget {
  const FeastMobileApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => EventVM()),
        ChangeNotifierProvider(create: (_) => AuthVM()),
        ChangeNotifierProvider(create: (_) => OtpVM()),
        ChangeNotifierProvider(create: (_) => RoutingVM()),
      ],
      child: MaterialApp.router(
        localizationsDelegates: const [
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        supportedLocales: const [
          Locale('en'),
          Locale('ru'),
        ],
        theme:
            ThemeData(platform: TargetPlatform.android, textTheme: textTheme),
        debugShowCheckedModeBanner: false,
        routerConfig: goRouter,
      ),
    );
  }
}
