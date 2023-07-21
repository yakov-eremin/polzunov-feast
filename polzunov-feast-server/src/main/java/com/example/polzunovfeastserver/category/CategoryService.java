package com.example.polzunovfeastserver.category;

import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import com.example.polzunovfeastserver.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.polzunovfeastserver.category.CategoryMapper.toCategory;
import static com.example.polzunovfeastserver.category.CategoryMapper.toCategoryEntity;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * @throws org.springframework.dao.DataIntegrityViolationException category with this name already exists
     */
    public Category addCategory(Category category) {
        category.setId(null);
        CategoryEntity entity = toCategoryEntity(category);
        return toCategory(categoryRepository.save(entity));
    }

    /**
     * @throws org.springframework.dao.DataIntegrityViolationException category with this name already exists
     */
    public Category updateCategoryById(Category category) {
        CategoryEntity entity = toCategoryEntity(category);
        return toCategory(categoryRepository.save(entity));
    }

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            return;
        }
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toCategory).collect(toList());
    }

    /**
     * @throws CategoryNotFoundException category not found
     */
    public Category getCategoryById(Long id) {
        return toCategory(getEntityById(id));
    }

    /**
     * @throws CategoryNotFoundException category not found
     */
    public CategoryEntity getEntityById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(
                String.format("Cannot get category with id=%d, because category not found", id)
        ));
    }

    /**
     * Duplicates will be ignored
     */
    public Set<CategoryEntity> getAllEntitiesById(Iterable<Long> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return new HashSet<>();
        }
        return categoryRepository.findAllByIdAsSet(ids);
    }
}
