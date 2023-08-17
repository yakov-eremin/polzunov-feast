package com.example.polzunovfeastserver.category;

import com.example.polzunovfeastserver.category.util.CategoriesTableKeys;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(name = CategoriesTableKeys.UNIQUE_NAME, columnNames = "category_name")
)
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "category_name")
    private String name;
}
