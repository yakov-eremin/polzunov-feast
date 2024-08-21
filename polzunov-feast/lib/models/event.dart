import 'package:feast_mobile/models/category.dart';
import 'package:feast_mobile/models/place.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class Event {
  final int id;
  final String name;
  final String description;
  final DateTimeRange timeRange;
  final int ageLimit;
  final bool canceled;
  final Place place;
  final List<CategoryModel> categories;
  final List<String> imageUrls;

  Event({
    required this.id,
    required this.name,
    required this.description,
    required this.timeRange,
    required this.ageLimit,
    required this.canceled,
    required this.place,
    required this.categories,
    required this.imageUrls,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    final categoriesJson = json['categories'] as List<dynamic>?;
    final categories = categoriesJson != null
        ? categoriesJson
            .map((model) =>
                CategoryModel.fromJson(model as Map<String, dynamic>))
            .toList()
        : <CategoryModel>[];

    return Event(
      id: json['id'] as int,
      name: json['name'] as String,
      description: json['description'] as String,
      timeRange: DateTimeRange(
          start: DateTime.parse(json['startTime'] as String),
          end: DateTime.parse(json['endTime'] as String)),
      ageLimit: json['ageLimit'] as int,
      canceled: json['canceled'] as bool,
      place: Place.fromJson(json['place'] as Map<String, dynamic>),
      categories: categories,
      imageUrls: List<String>.from((json['imageUrls'] as List)
          .map((e) => e.replaceAll('localhost', '10.0.2.2'))),
    );
  }
}

String dateFormatter(DateTimeRange timeRange) {
  String startTime;
  String endTime;
  if (timeRange.start.month != timeRange.end.month ||
      ((timeRange.start.day != timeRange.end.day) &&
          (timeRange.start.month == timeRange.end.month))) {
    startTime =
        "${DateFormat.MMMMd('ru').format(timeRange.start)} ${DateFormat('Hm').format(timeRange.start)}";
    endTime =
        "${DateFormat.MMMMd('ru').format(timeRange.end)} ${DateFormat('Hm').format(timeRange.end)}";
  } else {
    startTime = "${DateFormat.MMMMd('ru').format(timeRange.start)} ${DateFormat('Hm').format(timeRange.start)}";
    endTime = DateFormat('Hm').format(timeRange.end);
  }
  return '$startTime — $endTime';
}
