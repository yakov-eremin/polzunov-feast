import 'package:feast_mobile/view_models/events_view_model.dart';
import 'package:feast_mobile/models/category.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'category_grid_item.dart';

class CategoryGrid extends StatelessWidget {
  const CategoryGrid({
    super.key,
    this.elementOnTap,
    required this.existingCategories,
    required this.activeCategories,
    this.onItemTap,
  });

  final void Function()? elementOnTap;

  final List<CategoryModel> existingCategories;
  final List<CategoryModel> activeCategories;
  final void Function()? onItemTap;

  @override
  Widget build(BuildContext context) {
    final eventVM = context.watch<EventVM>();
    return GridView.builder(
      padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
      itemBuilder: (context, index) {
        final thisCategory = existingCategories[index];
        final active = activeCategories.contains(thisCategory);
        return CategoryGridItem(
          active: active,
          text: thisCategory.name,
          onTap: () {
            if (active) {
              eventVM.removeFilterCategory(thisCategory);
            } else {
              eventVM.addFilterCategory(thisCategory);
            }
          },
        );
      },
      itemCount: existingCategories.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        mainAxisSpacing: 10,
        crossAxisSpacing: 20,
        mainAxisExtent: 50,
      ),
    );
  }
}
