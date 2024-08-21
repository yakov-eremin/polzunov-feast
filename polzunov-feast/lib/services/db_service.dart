import 'dart:convert';
import 'dart:core';
import 'package:feast_mobile/models/category.dart';
import 'package:feast_mobile/models/event.dart';
import 'package:feast_mobile/models/filters.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';

abstract class DBService {
  static const Duration timeoutDuration = Duration(seconds: 5);
  static const String baseUrl = '10.0.2.2:8080';

  static Future<List<Event>> getRouteEvents(String authToken) async {
    final Response res = await http.get(Uri.http(baseUrl, '/route'), headers: {
      "Authorization": "Bearer $authToken"
    }).timeout(const Duration(seconds: 10));

    if (res.statusCode == 200) {
      final a = (json.decode(utf8.decode(res.bodyBytes))['nodes']);
      return List<Event>.from(a.map((model) => Event.fromJson(model['event'])));
    } else if (res.statusCode == 401) {
      throw TokenAuthFailed();
    } else {
      throw InternalException();
    }
  }

  static Future<void> putRoute(String authToken, List<Event> events) async {
    List<Map<String, int>> eventIdsList = [];
    for (Event event in events) {
      eventIdsList.add({"eventId": event.id});
    }
    String body = jsonEncode({"nodes": eventIdsList});
    final Response res = await http
        .put(Uri.http(baseUrl, '/route'),
            headers: {
              "Authorization": "Bearer $authToken",
              "Content-Type": 'application/json',
            },
            body: body)
        .timeout(const Duration(seconds: 10));

    if (res.statusCode == 200) {
      return;
    } else if (res.statusCode == 401) {
      throw TokenAuthFailed();
    } else {
      throw InternalException();
    }
  }

  static Future<List<Event>> getEvents(Filters filters, int pageIndex) async {
    Map<String, dynamic> params = {
      'canceled': false.toString(),
      'start': filters.start,
      'end': filters.end,
      'page': '$pageIndex',
      'size': '5'
    };
    if (filters.age != null) params.addAll({'age': filters.age.toString()});

    List<int> catIds =
        filters.categories.map((CategoryModel cat) => cat.id).toList();
    if (catIds.isNotEmpty) params.addAll({'catIds': catIds.join(',')});

    final res = await http
        .get(Uri.http(baseUrl, '/event', params))
        .timeout(timeoutDuration);

    if (res.statusCode == 200) {
      return List<Event>.from(json
          .decode(utf8.decode(res.bodyBytes))
          .map((model) => Event.fromJson(model)));
    } else {
      throw InternalException();
    }
  }

  static Future<List<CategoryModel>> getCategories() async {
    final res =
        await http.get(Uri.http(baseUrl, '/category')).timeout(timeoutDuration);

    if (res.statusCode == 200) {
      return List<CategoryModel>.from(json
          .decode(utf8.decode(res.bodyBytes))
          .map((model) => CategoryModel.fromJson(model)));
    } else {
      throw InternalException();
    }
  }

  static Future<String> userSignIn(
      String email, String password, String notificationToken) async {
    final Response response = await http
        .post(Uri.http(baseUrl, '/user/signin'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(<String, String?>{
              "email": email,
              "password": password,
              "notificationToken": notificationToken
            }))
        .timeout(timeoutDuration);

    if (response.statusCode == 200) {
      return (jsonDecode(response.body) as Map<String, dynamic>)['accessToken'];
    } else if (response.statusCode == 401) {
      throw SignInException(SignInFailure.wrongPassword);
    } else if (response.statusCode == 404) {
      throw SignInException(SignInFailure.emailAlreadyTaken);
    } else {
      throw InternalException();
    }
  }

  static Future<void> userCheck(String name, String email, String password,
      String notificationToken) async {
    final response = await http
        .post(Uri.http(baseUrl, '/user/check'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(<String, String?>{
              "name": name,
              "email": email,
              "password": password,
              "notificationToken": notificationToken
            }))
        .timeout(timeoutDuration);

    if (response.statusCode == 200) {
      return;
    } else if (response.statusCode == 409) {
      final msg = (jsonDecode(response.body) as Map<String, dynamic>)['code'];
      if (msg == 'EMAIL_ALREADY_EXISTS') {
        throw SignUpException(SignUpFailure.emailAlreadyExists);
      } else {
        throw SignUpException(SignUpFailure.phoneAlreadyExists);
      }
    } else {
      throw InternalException();
    }
  }

  static Future<String> userSignUp(String name, String email, String password,
      String notificationToken) async {
    final response = await http
        .post(Uri.http(baseUrl, '/user/signup'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(<String, String?>{
              "name": name,
              "email": email,
              "phone": null,
              "password": password,
              "notificationToken": notificationToken
            }))
        .timeout(timeoutDuration);

    if (response.statusCode == 200) {
      return (jsonDecode(response.body) as Map<String, dynamic>)['accessToken'];
    } else if (response.statusCode == 409) {
      final msg = (jsonDecode(response.body) as Map<String, dynamic>)["code"];
      if (msg == 'EMAIL_ALREADY_EXISTS') {
        throw SignUpException(SignUpFailure.emailAlreadyExists);
      } else {
        throw SignUpException(SignUpFailure.phoneAlreadyExists);
      }
    }
    throw InternalException();
  }

  static Future<bool> tokenCheck(String accessToken) async {
    final response = await http
        .get(Uri.http(baseUrl, '/auth/token/check'))
        .timeout(timeoutDuration);
    if (response.statusCode == 200) {
      return true;
    } else {
      return false;
    }
  }
}

class SignInException implements Exception {
  SignInException(this.type);
  SignInFailure type;
}

class TokenAuthFailed implements Exception {}

enum SignInFailure {
  wrongPassword,
  emailAlreadyTaken,
}

class SignUpException implements Exception {
  SignUpException(this.type);
  SignUpFailure type;
}

enum SignUpFailure {
  emailAlreadyExists,
  phoneAlreadyExists,
}

class InternalException implements Exception {}
