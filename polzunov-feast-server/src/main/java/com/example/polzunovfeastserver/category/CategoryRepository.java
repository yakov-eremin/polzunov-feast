package com.example.polzunovfeastserver.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("select c from CategoryEntity c where c.id in :ids")
    Set<CategoryEntity> findAllByIdAsSet(Iterable<Long> ids);
}
