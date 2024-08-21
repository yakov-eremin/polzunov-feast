import 'package:feast_mobile/models/event.dart';
import 'package:feast_mobile/models/route.dart';

class RouteInfo {
  final RouteModel walk;
  final RouteModel car;
  final double timeLeft;

  RouteInfo({required this.walk, required this.car, required this.timeLeft});

  bool get haveTimeForWalk => walk.time < timeLeft;
  bool get haveTimeForAuto => car.time < timeLeft;

  static double calcTimeLeft(Event start, Event end) {
    return (((end.timeRange.start.hour * 60) + end.timeRange.start.minute) -
            ((start.timeRange.end.hour * 60) + start.timeRange.end.minute))
        .toDouble();
  }
}

String doubleTimeToString(double time) {
  int inSecondsTime = (time * 60).floor();
  String string = '';
  if (inSecondsTime ~/ 3600 > 0) string += '${inSecondsTime ~/ 3600} ч ';
  if (inSecondsTime % 3600 ~/ 60 > 0) string += '${inSecondsTime % 3600 ~/ 60} мин';
  return string;
}

String doubleLengthToString(double length) {
  int inMeters = length.floor();
  String string = '';
  if (inMeters ~/ 1000 > 0) string += '${inMeters ~/ 1000} км ';
  if (inMeters % 1000 > 0) string += '${inMeters % 1000} м';
  return string;
}

enum RouteType { walk, car }
