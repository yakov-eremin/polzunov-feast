package com.example.polzunovfeastserver.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.CategoryApi;
import org.openapitools.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<Category> addCategory(Category category) {
        Category newCategory = categoryService.addCategory(category);
        log.info("Category with id={} added", newCategory.getId());
        return ResponseEntity.ok(newCategory);
    }

    @Override
    public ResponseEntity<Void> deleteCategoryById(Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        log.info("Found {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<Category> getCategoryById(Long id) {
        Category category = categoryService.getCategoryById(id);
        log.info("Category with id={} found", category.getId());
        return ResponseEntity.ok(category);
    }

    @Override
    public ResponseEntity<Category> updateCategoryById(Category category) {
        Category updatedCategory = categoryService.updateCategoryById(category);
        log.info("Category with id={} updated", updatedCategory.getId());
        return ResponseEntity.ok(updatedCategory);
    }
}
