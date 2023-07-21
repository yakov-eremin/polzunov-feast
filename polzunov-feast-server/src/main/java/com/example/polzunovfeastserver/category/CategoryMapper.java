package com.example.polzunovfeastserver.category;

import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import org.openapitools.model.Category;

public final class CategoryMapper {
    private CategoryMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static CategoryEntity toCategoryEntity(Category category) {
        return new CategoryEntity(
                category.getId(),
                category.getName().trim()
        );
    }

    public static Category toCategory(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setName(categoryEntity.getName());
        return category;
    }
}
