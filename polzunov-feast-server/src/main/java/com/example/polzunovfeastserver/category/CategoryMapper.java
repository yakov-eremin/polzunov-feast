package com.example.polzunovfeastserver.category;

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
        return new Category(
                categoryEntity.getId(),
                categoryEntity.getName()
        );
    }
}
