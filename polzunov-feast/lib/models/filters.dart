import 'package:feast_mobile/models/category.dart';
import 'package:flutter/material.dart';

class Filters {
  late int? age;
  late List<CategoryModel> categories;
  late DateTimeRange timeRange;

  String get start => timeRange.start.toIso8601String();
  String get end => timeRange.end.toIso8601String();

  resetFilters() {
    final DateTime now = DateTime.now();
    categories.clear();
    timeRange = DateTimeRange(
        start: DateTime.utc(now.year, now.month, now.day, 0, 0, 0),
        end: DateTime.utc(now.year, now.month + 2, now.day, 23, 59, 0));
    age = null;
  }

  Filters(this.age, this.categories, this.timeRange);

  Filters.empty() {
    final DateTime now = DateTime.now();
    categories = [];
    timeRange = DateTimeRange(
        start: DateTime.utc(now.year, now.month, now.day, 0, 0, 0),
        end: DateTime.utc(now.year, now.month + 2, now.day, 23, 59, 0));
    age = null;
  }

  Filters copyWith({
    int? age,
    List<CategoryModel>? categories,
    DateTimeRange? timeRange,
  }) {
    return Filters(
      age ?? this.age,
      categories ?? List.from(this.categories),
      timeRange ?? this.timeRange
    );
  }
}
