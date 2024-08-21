import 'package:flutter/material.dart';

class EmptyListPlaceholder extends StatelessWidget {
  const EmptyListPlaceholder({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(30.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.list_alt_rounded,
            color: Colors.grey.withOpacity(0.35),
            size: 50,
          ),
          const SizedBox(height: 20),
          Text(
            'Нет мероприятий',
            style: Theme.of(context).textTheme.labelLarge,
          ),
          const SizedBox(height: 10),
          Text(
            'По вашему запросу не найдено мероприятий. Попробуйте изменить параметры запроса',
            style: Theme.of(context).textTheme.labelSmall,
            textAlign: TextAlign.center,
          )
        ],
      ),
    );
  }
}
