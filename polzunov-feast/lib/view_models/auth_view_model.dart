import 'dart:async';
import 'dart:io';

import 'package:feast_mobile/models/user.dart';
import 'package:feast_mobile/services/db_service.dart';
import 'package:flutter/material.dart';

enum AuthMode { signup, signin }

class AuthVM extends ChangeNotifier {
  final RegExp _emailRegExp = RegExp(
      r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+");

  final RegExp _passwordRegExp = RegExp(
      r'^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?^&])[A-Za-z\d@$!%*#?^&]{3,}$');

  late AuthMode authMode;
  User user = User.empty();
  bool loading = false;
  String? emailError;
  String? passwordError;
  String? nameError;
  bool canContinue = false;
  bool passwordObscured = true;
  ErrorMessage? errorMessage;
  bool logedIn = false;

  clearFields() {
    emailError = null;
    passwordError = null;
    user = User.empty();
    canContinue = false;
    passwordObscured = true;
  }

  setLogedIn(bool logedStatus) {
    logedIn = logedStatus;
    if (logedStatus == false) {
      clearFields();
    }
    notifyListeners();
  }

  setUser(User? newUser) {
    notifyListeners();
  }

  setLoading() {
    loading = !loading;
    notifyListeners();
  }

  emailChanged(String? newVal) {
    if (newVal != null) user.email = newVal;
    canContinue = true;
    if (user.email == '') {
      emailError = "Заполните поле";
      canContinue = false;
    } else if (!_emailRegExp.hasMatch(user.email)) {
      emailError = "Не верный формат";
      canContinue = false;
    } else {
      emailError = null;
      if (authMode == AuthMode.signin) {
        if (passwordError != null || user.password == '') canContinue = false;
      } else {
        if (passwordError != null ||
            user.password == '' ||
            nameError != null ||
            user.name == '') canContinue = false;
      }
    }
    notifyListeners();
  }

  passwordChanged(String? newVal) {
    if (newVal != null) user.password = newVal;
    canContinue = true;
    if (user.password == '') {
      passwordError = "Заполните поле";
      canContinue = false;
    } else if (user.password.length < 8) {
      passwordError = 'Введите более 7 символов';
      canContinue = false;
    } else if (!_passwordRegExp.hasMatch(user.password)) {
      List additions = [];
      if (!user.password.contains(RegExp(r'[a-zA-Z]'))) additions.add('буквы');
      if (!user.password.contains(RegExp(r'[@$!%*#?^&]'))) {
        additions.add('спец.символы');
      }
      if (!user.password.contains(RegExp(r'[0-9]'))) additions.add('цифры');
      if (additions.isNotEmpty) {
        passwordError = 'Добавьте: ${additions.join(', ')}';
      } else {
        passwordError = 'Уберите некорректные символы';
      }

      canContinue = false;
    } else {
      passwordError = null;
      if (authMode == AuthMode.signin) {
        if (emailError != null || user.email == '') canContinue = false;
      } else {
        if (emailError != null ||
            user.email == '' ||
            nameError != null ||
            user.name == '') canContinue = false;
      }
    }
    notifyListeners();
  }

  nameChanged(String? newVal) {
    if (newVal != null) user.name = newVal;

    if (user.name.length < 2) {
      nameError = "Введите больше одного символа";
      canContinue = false;
    } else {
      nameError = null;
      if (emailError != null ||
          user.email == '' ||
          passwordError != null ||
          user.password == '') {
        canContinue = false;
      } else {
        canContinue = true;
      }
    }
    notifyListeners();
  }

  passwordVisibilityChanged() {
    passwordObscured = !passwordObscured;
    notifyListeners();
  }

  setErrorMessage(ErrorMessage? msg) {
    errorMessage = msg;
    notifyListeners();
  }

  Future<bool> signin() async {
    try {
      setLoading();
      user.accessToken = await DBService.userSignIn(
          user.email, user.password, user.accessToken);
      setLoading();
      setLogedIn(true);
      return true;
    } on SignInException catch (e) {
      if (e.type == SignInFailure.wrongPassword) {
        passwordError = 'Неверный пароль';
        canContinue = false;
      } else if (e.type == SignInFailure.emailAlreadyTaken) {
        emailError = 'Такой email не зарегистрирован';
        canContinue = false;
      }
      setLoading();
      return false;
    } on TimeoutException catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Ошибка связи',
        description: 'Слабое интернет-соединение',
      ));
      setLoading();
      return false;
    } on SocketException catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Ошибка связи',
        description: 'Проверьте интернет-соединение',
      ));
      setLoading();
      return false;
    } on Exception catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Неизвестная ошибка',
        description: 'Попробуйте позже',
      ));
      setLoading();
      return false;
    }
  }

  Future<bool> usercheck() async {
    try {
      setLoading();
      await DBService.userCheck(
          user.name, user.email, user.password, user.accessToken);
      setLoading();
      return (true);
    } on SignUpException catch (e) {
      if (e.type == SignUpFailure.emailAlreadyExists) {
        emailError = 'Email занят';
        canContinue = false;
      } else if (e.type == SignUpFailure.phoneAlreadyExists) {}
      setLoading();
      return false;
    } on TimeoutException catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Ошибка связи',
        description: 'Слабое интернет-соединение',
      ));
      setLoading();
      return false;
    } on SocketException catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Ошибка связи',
        description: 'Проверьте интернет-соединение',
      ));
      setLoading();
      return false;
    } on Exception catch (_) {
      setErrorMessage(ErrorMessage(
        title: 'Неизвестная ошибка',
        description: 'Попробуйте позже',
      ));
      setLoading();
      return false;
    }
  }

  Future<bool> tokenCheck() async {
    try {
      // setLoading();
      final bool res = await DBService.tokenCheck(user.accessToken);
      // setLoading();
      return (res);
    } on TimeoutException catch (_) {
      // setErrorMessage(ErrorMessage(
      //   title: 'Ошибка связи',
      //   description: 'Слабое интернет-соединение',
      // ));
      // setLoading();
      return false;
    } on SocketException catch (_) {
      // setErrorMessage(ErrorMessage(
      //   title: 'Ошибка связи',
      //   description: 'Проверьте интернет-соединение',
      // ));
      // setLoading();
      return false;
    } on Exception catch (_) {
      // setErrorMessage(ErrorMessage(
      //   title: 'Неизвестная ошибка',
      //   description: 'Попробуйте позже',
      // ));
      // setLoading();
      return false;
    }
  }
}

class ErrorMessage {
  ErrorMessage({required this.title, required this.description});

  String title;
  String description;
}
