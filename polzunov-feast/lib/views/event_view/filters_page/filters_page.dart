import 'package:feast_mobile/views/event_view/filters_page/widgets/date_duration_picker.dart';
import 'package:feast_mobile/routes/routes.dart';
import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'widgets/age_input.dart';
import 'widgets/exit_alert_dialog.dart';
import 'widgets/show_events_button.dart';
import 'widgets/category_grid.dart';
import 'widgets/time_picker.dart';

class FiltersPage extends StatelessWidget {
  const FiltersPage({super.key});

  @override
  Widget build(BuildContext context) {
    final eventVM = context.watch<EventVM>();
    return PopScope(
      canPop: true,
      child: Scaffold(
        backgroundColor: Colors.white,
        appBar: AppBar(
          leading: IconButton(
              onPressed: () async {
                if (eventVM.filtersChanged()) {
                  await showExitDialog(
                    context,
                    onSubmit: () {
                      eventVM.applyFiltersChanges();
                      goRouter.pop();
                      goRouter.go('/event_list');
                    },
                    onCancel: () {
                      goRouter.pop();
                      goRouter.pop();
                    },
                  );
                } else {
                  goRouter.pop();
                }
              },
              icon: const Icon(Icons.arrow_back)),
          scrolledUnderElevation: 0.0,
          backgroundColor: Colors.white,
          centerTitle: false,
          title: Text('Выберите фильтры',
              style: Theme.of(context).textTheme.titleMedium),
          actions: [
            Directionality(
              textDirection: TextDirection.rtl,
              child: TextButton.icon(
                  onPressed: () => eventVM.resetFilters(),
                  label: const Text('Сбросить',
                      style: TextStyle(color: Colors.blue)),
                  icon: const Icon(Icons.replay_rounded, color: Colors.blue)),
            )
          ],
        ),
        body: Padding(
          padding: const EdgeInsets.fromLTRB(10, 20, 10, 10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Категории', style: Theme.of(context).textTheme.labelLarge),
              eventVM.categoryLoading
                  ? const Flexible(
                      flex: 1,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Center(
                              child: CircularProgressIndicator(
                            color: Colors.blue,
                          )),
                        ],
                      ))
                  : eventVM.categoryLoadingError
                      ? Flexible(
                          flex: 1,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Center(
                                  child: Text(
                                'Ошибка загрузки категорий',
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.w500,
                                  color: Colors.red.shade800,
                                ),
                              )),
                              Center(
                                child: ElevatedButton(
                                    style: ElevatedButton.styleFrom(
                                        backgroundColor: Colors.grey.shade400,
                                        foregroundColor: Colors.white),
                                    onPressed: () {
                                      eventVM.getCategories();
                                    },
                                    child: const Text(
                                      'Загрузить',
                                      style: TextStyle(fontSize: 14),
                                    )),
                              )
                            ],
                          ))
                      : eventVM.existingCategories.isNotEmpty
                          ? Flexible(
                              flex: 1,
                              child: CategoryGrid(
                                  existingCategories:
                                      eventVM.existingCategories,
                                  activeCategories: eventVM.filterCategories),
                            )
                          : Flexible(
                              flex: 1,
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Center(
                                      child: Text(
                                    'Список категорий пуст',
                                    style:
                                        Theme.of(context).textTheme.titleSmall,
                                  )),
                                  Center(
                                    child: ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                            backgroundColor:
                                                Colors.grey.shade400,
                                            foregroundColor: Colors.white),
                                        onPressed: () {
                                          eventVM.getCategories();
                                        },
                                        child: const Text(
                                          'Загрузить',
                                          style: TextStyle(fontSize: 14),
                                        )),
                                  )
                                ],
                              )),
              const Divider(),
              Flexible(
                flex: 1,
                child: Column(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Text(
                          'От',
                          style: Theme.of(context).textTheme.labelLarge,
                        ),
                        TimePicker(
                            time: eventVM.startTime,
                            onTap: () async {
                              final TimeOfDay? time =
                                  await showMyTimePicker(context);
                              if (time != null) {
                                eventVM.setFilterStartTime(
                                    time.hour, time.minute);
                              }
                            }),
                        Text(
                          'До',
                          style: Theme.of(context).textTheme.labelLarge,
                        ),
                        TimePicker(
                            time: eventVM.endTime,
                            onTap: () async {
                              final TimeOfDay? time =
                                  await showMyTimePicker(context);
                              if (time != null) {
                                eventVM.setFilterEndTime(
                                    time.hour, time.minute);
                              }
                            })
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Text('Даты ',
                            style: Theme.of(context).textTheme.labelLarge),
                        DateDurationPicker(
                          startDate: eventVM.startDate,
                          endDate: eventVM.endDate,
                          onTap: () async {
                            final DateTimeRange? range =
                                await showMyDateRangePicker(context);
                            if (range != null) {
                              eventVM.setFiltersDates(range);
                            }
                          },
                        ),
                      ],
                    ),
                    AgeInput(
                        controller: eventVM.controller,
                        onChanged: (newValue) => eventVM.setFilterAge(
                            newValue == '' ? null : int.parse(newValue))),
                  ],
                ),
              ),
              ShowEventsButton(
                onPressed: () {
                  if (eventVM.filtersChanged()) {
                    eventVM.applyFiltersChanges();
                    goRouter.go('/event_list');
                  } else {
                    goRouter.go('/event_list');
                  }
                },
              )
            ],
          ),
        ),
      ),
    );
  }
}

Future<dynamic> showExitDialog(BuildContext context,
    {void Function()? onSubmit, void Function()? onCancel}) {
  return showAdaptiveDialog(
      context: context,
      builder: (BuildContext context) => ExitAlertDialog(
            title: 'Вы изменили фильтры',
            description: 'Хотите применить изменения?',
            onSubmit: onSubmit,
            onCancel: onCancel,
          ));
}

Future<DateTimeRange?> showMyDateRangePicker(BuildContext context) {
  DateTime now = DateTime.now();
  return showDateRangePicker(
    context: context,
    locale: const Locale('ru'),
    firstDate: now,
    lastDate: DateTime(now.year, now.month + 2, now.day, 0, 0, 0),
  );
}

Future<DateTime?> showDateTimePicker(BuildContext context) {
  DateTime now = DateTime.now();
  return showDatePicker(
    context: context,
    firstDate: DateTime(now.year, now.month, now.day, 0, 0, 0),
    lastDate: DateTime(now.year, now.month + 2, now.day, 0, 0, 0),
  );
}

Future<TimeOfDay?> showMyTimePicker(BuildContext context) {
  return showTimePicker(
    context: context,
    helpText: 'Выберите время',
    cancelText: 'Отмена',
    errorInvalidText: 'Введите корректное время',
    confirmText: 'ОК',
    hourLabelText: 'Часы',
    minuteLabelText: 'Минуты',
    initialTime: const TimeOfDay(hour: 0, minute: 0),
    initialEntryMode: TimePickerEntryMode.dial,
    builder: (BuildContext context, Widget? child) {
      return Theme(
        data: Theme.of(context).copyWith(
          materialTapTargetSize: MaterialTapTargetSize.padded,
        ),
        child: Directionality(
          textDirection: TextDirection.ltr,
          child: MediaQuery(
            data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
            child: child!,
          ),
        ),
      );
    },
  );
}
