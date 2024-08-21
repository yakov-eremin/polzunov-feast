import 'package:feast_mobile/models/category.dart';
import 'package:feast_mobile/models/event.dart';
import 'package:feast_mobile/models/route.dart';
import 'package:feast_mobile/models/route_info.dart';
import 'package:feast_mobile/services/geolocation_service.dart';
import 'package:feast_mobile/services/db_service.dart';
import 'package:feast_mobile/services/routing_service.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:latlong2/latlong.dart';
import '../models/filters.dart';
import 'package:intl/intl.dart';

class EventVM extends ChangeNotifier {
  int eventPageIndex = 0;
  bool _loading = false;
  List<Event> _eventList = [];
  late Event _selectedEvent;
  late RouteInfo routeInfo;
  List<CategoryModel> _existingCategories = [];
  TextEditingController controller = TextEditingController();
  Event? lastRouteEvent;
  bool walkRouteShowing = true;
  bool routeBuildingInProgress = false;

  late bool eventLoadingError;
  bool eventLoading = false;

  late bool categoryLoadingError;
  bool categoryLoading = false;

  ScrollController scrollController = ScrollController();

  Filters _filters = Filters.empty();
  late Filters _filtersDup;

  bool get loading => _loading;
  List<Event> get eventList => _eventList;
  List<CategoryModel> get existingCategories => _existingCategories;
  Event get selectedEvent => _selectedEvent;
  String get startTime =>
      DateFormat.Hm('ru').format(_filtersDup.timeRange.start);
  String get endTime => DateFormat.Hm('ru').format(_filtersDup.timeRange.end);
  String get startDate =>
      DateFormat.yMd('ru').format(_filtersDup.timeRange.start);
  String get endDate => DateFormat.yMd('ru').format(_filtersDup.timeRange.end);
  List<CategoryModel> get filterCategories => _filtersDup.categories;

  EventVM() {
    scrollController.addListener(() {
      if (scrollController.position.pixels ==
          scrollController.position.maxScrollExtent) {
        addEvents();
      }
    });
    getEvents();
    getCategories();
  }

  changeRouteShowing() {
    walkRouteShowing = !walkRouteShowing;
    notifyListeners();
  }

  createFiltersDup() {
    _filtersDup = _filters.copyWith();
  }

  bool filtersChanged() {
    return (!listEquals(_filters.categories, _filtersDup.categories) ||
        (_filters.age != _filtersDup.age) ||
        (_filters.start != _filtersDup.start) ||
        (_filters.end != _filtersDup.end));
  }

  applyFiltersChanges() {
    eventPageIndex = 0;
    _filters = _filtersDup.copyWith();
    getEvents();
  }

  setFilterAge(int? age) {
    _filtersDup.age = age;
  }

  addFilterCategory(CategoryModel newCategory) {
    _filtersDup.categories.add(newCategory);
    notifyListeners();
  }

  removeFilterCategory(CategoryModel category) {
    _filtersDup.categories.remove(category);
    notifyListeners();
  }

  setFilterStartTime(int hour, int minute) {
    if (!(_filtersDup.timeRange.start.month ==
            _filtersDup.timeRange.end.month &&
        _filtersDup.timeRange.start.day == _filtersDup.timeRange.end.day &&
        (hour > _filtersDup.timeRange.end.hour ||
            hour == _filtersDup.timeRange.end.hour &&
                minute > _filtersDup.timeRange.end.minute))) {
      _filtersDup.timeRange = DateTimeRange(
        start: DateTime.utc(
            _filtersDup.timeRange.start.year,
            _filtersDup.timeRange.start.month,
            _filtersDup.timeRange.start.day,
            hour,
            minute,
            0),
        end: _filtersDup.timeRange.end,
      );
    }
    notifyListeners();
  }

  setFilterEndTime(int hour, int minute) {
    if (!(_filtersDup.timeRange.start.month ==
            _filtersDup.timeRange.end.month &&
        _filtersDup.timeRange.start.day == _filtersDup.timeRange.end.day &&
        (hour < _filtersDup.timeRange.start.hour ||
            hour == _filtersDup.timeRange.end.hour &&
                minute < _filtersDup.timeRange.start.minute))) {
      _filtersDup.timeRange = DateTimeRange(
          start: _filtersDup.timeRange.start,
          end: DateTime.utc(
            _filtersDup.timeRange.end.year,
            _filtersDup.timeRange.end.month,
            _filtersDup.timeRange.end.day,
            hour,
            minute,
            0,
          ));
    }
    notifyListeners();
  }

  setFiltersDates(DateTimeRange timeRange) {
    if (timeRange.start.month == timeRange.end.month &&
        timeRange.start.day == timeRange.end.day &&
        (_filtersDup.timeRange.start.hour > _filtersDup.timeRange.end.hour ||
            (_filtersDup.timeRange.start.hour ==
                    _filtersDup.timeRange.end.hour &&
                _filtersDup.timeRange.start.minute >
                    _filtersDup.timeRange.end.minute))) {
    } else {
      setFiltersStartDate(
          timeRange.start.year, timeRange.start.month, timeRange.start.day);
      setFiltersEndDate(
          timeRange.end.year, timeRange.end.month, timeRange.end.day);
    }
  }

  setFiltersStartDate(int year, int month, int day) {
    _filtersDup.timeRange = DateTimeRange(
      start: DateTime.utc(
        year,
        month,
        day,
        _filtersDup.timeRange.start.hour,
        _filtersDup.timeRange.start.minute,
        0,
      ),
      end: _filtersDup.timeRange.end,
    );
    notifyListeners();
  }

  setFiltersEndDate(int year, int month, int day) {
    _filtersDup.timeRange = DateTimeRange(
      start: _filtersDup.timeRange.start,
      end: DateTime.utc(
        year,
        month,
        day,
        _filtersDup.timeRange.end.hour,
        _filtersDup.timeRange.end.minute,
        0,
      ),
    );
    notifyListeners();
  }

  getLastRouteEvent(Event? event) {
    lastRouteEvent = event;
    if (lastRouteEvent != null) {
      _filters = _filters.copyWith(
          timeRange: DateTimeRange(
              start: lastRouteEvent!.timeRange.end,
              end: _filters.timeRange.end));
    } else {
      final DateTime now = DateTime.now();
      _filters = _filters.copyWith(
          timeRange: DateTimeRange(
              start: DateTime.utc(now.year, now.month, now.day, 0, 0, 0),
              end: _filters.timeRange.end));
    }
    eventPageIndex = 0;
    getEvents();
  }

  setLoading(bool loading) async {
    _loading = loading;
    notifyListeners();
  }

  setEventList(List<Event> eventList) {
    _eventList = eventList;
  }

  addEvents() async {
    setLoading(true);
    try {
      final List<Event> temp =
          await DBService.getEvents(_filters, eventPageIndex + 1);
      if (temp.isNotEmpty) {
        eventPageIndex += 1;
        eventList.addAll(temp);
      }
    } finally {
      setLoading(false);
    }
  }

  getEvents() async {
    try {
      eventLoading = true;
      eventLoadingError = false;
      notifyListeners();
      setEventList(await DBService.getEvents(_filters, eventPageIndex));
    } catch (e) {
      eventLoadingError = true;
    } finally {
      eventLoading = false;
      notifyListeners();
    }
  }

  setCategories(List<CategoryModel> categories) {
    _existingCategories = categories;
  }

  setSelectedEvent(Event selectedEvent) async {
    _selectedEvent = selectedEvent;
    routeBuildingInProgress = true;
    notifyListeners();

    if (lastRouteEvent == null) {
      final Position userPos =
          await GeolocationService.determineCurrentPosition();
      final RouteModel walkRoute = await RoutingService.getRouteFullFromCoord(
        LatLng(userPos.latitude, userPos.longitude),
        _selectedEvent.place.address,
        RouteType.walk,
      );
      final RouteModel carRoute = await RoutingService.getRouteFullFromCoord(
        LatLng(userPos.latitude, userPos.longitude),
        _selectedEvent.place.address,
        RouteType.car,
      );
      final double timeLeft = _selectedEvent.timeRange.start
              .difference(DateTime.now())
              .inSeconds
              .toDouble() /
          60;
      routeInfo = RouteInfo(walk: walkRoute, car: carRoute, timeLeft: timeLeft);
      routeBuildingInProgress = false;
      notifyListeners();
    } else {
      final RouteModel walkRoute = await RoutingService.getRouteFullFromAddress(
        lastRouteEvent!.place.address,
        _selectedEvent.place.address,
        RouteType.walk,
      );
      final RouteModel carRoute = await RoutingService.getRouteFullFromAddress(
        lastRouteEvent!.place.address,
        _selectedEvent.place.address,
        RouteType.car,
      );
      final double timeLeft = _selectedEvent.timeRange.start
              .difference(lastRouteEvent!.timeRange.end)
              .inSeconds
              .toDouble() /
          60;
      routeInfo = RouteInfo(walk: walkRoute, car: carRoute, timeLeft: timeLeft);
      routeBuildingInProgress = false;
      notifyListeners();
    }
  }

  getCategories() async {
    try {
      categoryLoading = true;
      categoryLoadingError = false;
      notifyListeners();
      setCategories(await DBService.getCategories());
    } catch (e) {
      categoryLoadingError = true;
    } finally {
      categoryLoading = false;
      notifyListeners();
    }
  }

  resetFilters() {
    _filtersDup.resetFilters();
    if (lastRouteEvent != null) {
      _filtersDup = _filtersDup.copyWith(
          timeRange: DateTimeRange(
              start: lastRouteEvent!.timeRange.end,
              end: _filtersDup.timeRange.end));
    }
    controller.text = '';
    notifyListeners();
  }
}
