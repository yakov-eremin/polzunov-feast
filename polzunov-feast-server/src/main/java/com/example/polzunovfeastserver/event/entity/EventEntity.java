package com.example.polzunovfeastserver.event.entity;

import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import com.example.polzunovfeastserver.event.image.entity.ImageEntity;
import com.example.polzunovfeastserver.event.util.table_key.EventCategoriesTableKeys;
import com.example.polzunovfeastserver.event.util.table_key.EventTableKeys;
import com.example.polzunovfeastserver.event.image.util.table_key.ImageTableKeys;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

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

    @Size(max = 1024)
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

    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "event_categories",
            joinColumns = @JoinColumn(
                    name = "event_id",
                    foreignKey = @ForeignKey(name = EventCategoriesTableKeys.FOREIGN_EVENT, value = NO_CONSTRAINT)
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id",
                    foreignKey = @ForeignKey(name = EventCategoriesTableKeys.FOREIGN_CATEGORY, value = CONSTRAINT)
            )
    )
    private Set<CategoryEntity> categories;

    private int ageLimit;

    @OneToOne
    @JoinColumn(
            name = "main_image_id",
            foreignKey = @ForeignKey(name = EventTableKeys.FOREIGN_MAIN_IMAGE_URL, value = CONSTRAINT)
    )
    private ImageEntity mainImage;

    @Setter(AccessLevel.NONE)
    @OneToMany
    @JoinColumn(
            name = "event_id",
            foreignKey = @ForeignKey(name = ImageTableKeys.FOREIGN_EVENT, value = CONSTRAINT)
    )
    private Set<ImageEntity> images;
}
