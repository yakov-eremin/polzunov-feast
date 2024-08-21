import 'package:flutter/material.dart';

class RequreLoginPlaceholder extends StatelessWidget {
  const RequreLoginPlaceholder({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: Padding(
      padding: const EdgeInsets.all(30.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.person_off,
            color: Colors.grey.withOpacity(0.45),
            size: 40,
          ),
          const SizedBox(height: 20),
          Text(
            'Расписание не доступно',
            style: Theme.of(context).textTheme.labelLarge,
          ),
          const SizedBox(height: 10),
          Text(
            'Зарегистрируйтесь для получения доступа к индивидуальному расписанию',
            style: Theme.of(context).textTheme.labelSmall,
            textAlign: TextAlign.center,
          )
        ],
      ),
    ));
  }
}
