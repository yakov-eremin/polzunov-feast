package com.example.polzunovfeastserver.event.entity;

import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import com.example.polzunovfeastserver.event.util.EventTableKeys;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

import static com.example.polzunovfeastserver.event.util.EventTableKeys.FOREIGN_CATEGORY;
import static com.example.polzunovfeastserver.event.util.EventTableKeys.FOREIGN_EVENT;
import static jakarta.persistence.ConstraintMode.CONSTRAINT;
import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

@Table(name = "events")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name")
    private String name;

    private String description;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @ManyToOne
    @JoinColumn(
            name = "place_id",
            foreignKey = @ForeignKey(name = EventTableKeys.FOREIGN_PLACE, value = CONSTRAINT)
    )
    private PlaceEntity place;

    private boolean canceled;

    /**
     * Do not use getCategories().add()/remove()/clear. Use
     * {@link #addCategory} and {@link #removeCategory} instead
     */
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "event_categories", //TODO Разобраться, какие ограничения надо поставить на внешние ключи
            joinColumns = @JoinColumn(
                    name = "event_id",
                    foreignKey = @ForeignKey(name = FOREIGN_EVENT, value = NO_CONSTRAINT)
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id",
                    foreignKey = @ForeignKey(name = FOREIGN_CATEGORY, value = CONSTRAINT)
            )
    )
    private Set<CategoryEntity> categories;

    private int ageLimit;


    public void addCategory(CategoryEntity category) {
        categories.add(category);
    }

    public void removeCategory(CategoryEntity category) {
        categories.remove(category);
    }

    public void addCategories(Set<CategoryEntity> categories) {
        this.categories.addAll(categories);
    }

    public void removeCategories(Set<CategoryEntity> categories) {
        this.categories.removeAll(categories);
    }

    public void clearCategories() {
        this.categories.clear();
    }

}
