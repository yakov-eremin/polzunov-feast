import 'package:feast_mobile/views/auth/otp_page/ui/otp_page.dart';
import 'package:feast_mobile/views/auth/signup_page/ui/signup_page.dart';
import 'package:feast_mobile/views/auth/successfull_auth_page/successfull_auth_page.dart';
import 'package:feast_mobile/views/event_view/event_details_page.dart/event_details_page.dart';
import 'package:feast_mobile/views/event_view/event_list_page/event_list_page.dart';

import 'package:feast_mobile/views/auth/profile_base_page/profile_base_page.dart';
import 'package:feast_mobile/views/auth/signin_page/ui/signin_page.dart';
import 'package:feast_mobile/views/event_view/event_route_details_page.dart/event_route_details_page.dart';
import 'package:feast_mobile/views/event_view/filters_page/filters_page.dart';
import 'package:feast_mobile/views/route_views/event_details_page/route_event_details_page.dart';
import 'package:feast_mobile/views/route_views/route_details_page/route_details_page.dart';
import 'package:feast_mobile/views/route_views/route_list_page/route_list_page.dart';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

final rootNavigatorKey = GlobalKey<NavigatorState>();
final shellNavigatorKeyEvents = GlobalKey<NavigatorState>();
final shellNavigatorKeyProfile = GlobalKey<NavigatorState>();

GoRouter goRouter = GoRouter(initialLocation: '/profile', routes: [
  GoRoute(
      path: '/route_details',
      pageBuilder: (context, state) {
        return const NoTransitionPage(child: RouteDetailsPage());
      }),
  GoRoute(
      path: '/routes_event_details',
      pageBuilder: (context, state) {
        return const NoTransitionPage(child: RouteEventDetailsPage());
      }),
  GoRoute(
      path: '/event_route',
      pageBuilder: (context, state) {
        return const NoTransitionPage(child: EventRouteDetailsPage());
      }),
  GoRoute(
      path: '/event_details',
      pageBuilder: (context, state) {
        return const NoTransitionPage(child: EventDetailsPage());
      }),
  GoRoute(
      path: '/event_filters',
      pageBuilder: (context, state) {
        return const NoTransitionPage(child: FiltersPage());
      }),
  StatefulShellRoute.indexedStack(
      builder: (context, state, navigationShell) {
        return ScaffoldWithNestedNavigation(
          navigationShell: navigationShell,
        );
      },
      branches: [
        StatefulShellBranch(routes: [
          GoRoute(
              path: '/event_list',
              pageBuilder: (context, state) =>
                  const NoTransitionPage(child: EventListPage()))
        ]),
        StatefulShellBranch(routes: [
          GoRoute(
              path: '/routes',
              pageBuilder: (context, state) =>
                  const NoTransitionPage(child: RouteListPage())),
        ]),
        StatefulShellBranch(navigatorKey: shellNavigatorKeyProfile, routes: [
          GoRoute(
              path: '/profile',
              pageBuilder: (context, state) =>
                  const NoTransitionPage(child: ProfileBasePage()),
              routes: [
                GoRoute(
                    path: 'signin',
                    pageBuilder: (context, state) =>
                        const NoTransitionPage(child: SigninPage()),
                    routes: [
                      GoRoute(
                          path: 'success',
                          pageBuilder: (context, state) =>
                              const NoTransitionPage(child: SuccessfullAuthPage()))
                    ]),
                GoRoute(
                    path: 'signup',
                    pageBuilder: (context, state) =>
                        const NoTransitionPage(child: SignupPage()),
                    routes: [
                      GoRoute(
                          path: 'otp',
                          pageBuilder: (context, state) =>
                              const NoTransitionPage(child: OTPPage()),
                          routes: [
                            GoRoute(
                                path: 'success',
                                pageBuilder: (context, state) =>
                                    const NoTransitionPage(
                                        child: SuccessfullAuthPage()))
                          ]),
                    ])
              ])
        ])
      ])
]);

class ScaffoldWithNestedNavigation extends StatelessWidget {
  const ScaffoldWithNestedNavigation({
    super.key,
    required this.navigationShell,
  });
  final StatefulNavigationShell navigationShell;

  void _goBranch(int index) {
    navigationShell.goBranch(
      index,
      initialLocation: index == navigationShell.currentIndex,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: navigationShell,
      bottomNavigationBar: NavigationBar(
        elevation: 0,
        selectedIndex: navigationShell.currentIndex,
        indicatorColor: Colors.white,
        destinations: const [
          NavigationDestination(
              icon: Icon(Icons.flag),
              selectedIcon: Icon(Icons.flag, color: Colors.blue),
              label: 'Мероприятия'),
          NavigationDestination(
              icon: Icon(Icons.list_alt),
              selectedIcon: Icon(Icons.list_alt, color: Colors.blue),
              label: 'Расписание'),
          NavigationDestination(
              icon: Icon(Icons.person),
              selectedIcon: Icon(Icons.person, color: Colors.blue),
              label: 'Профиль')
        ],
        onDestinationSelected: _goBranch,
      ),
    );
  }
}
