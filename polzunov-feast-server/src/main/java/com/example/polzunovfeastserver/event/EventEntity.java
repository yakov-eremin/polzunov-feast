package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.category.CategoryEntity;
import com.example.polzunovfeastserver.event.util.table_key.EventCategoriesTableKeys;
import com.example.polzunovfeastserver.event.util.table_key.EventsTableKeys;
import com.example.polzunovfeastserver.image.ImageEntity;
import com.example.polzunovfeastserver.image.util.ImagesTableKeys;
import com.example.polzunovfeastserver.place.PlaceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Objects;
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
            foreignKey = @ForeignKey(name = EventsTableKeys.FOREIGN_PLACE, value = CONSTRAINT)
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
    @ToString.Exclude
    private Set<CategoryEntity> categories;

    private int ageLimit;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "main_image_id",
            foreignKey = @ForeignKey(name = EventsTableKeys.FOREIGN_MAIN_IMAGE_URL, value = CONSTRAINT)
    )
    private ImageEntity mainImage;

    @Setter(AccessLevel.NONE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "event_id",
            foreignKey = @ForeignKey(name = ImagesTableKeys.FOREIGN_EVENT, value = CONSTRAINT)
    )
    private Set<ImageEntity> images;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EventEntity that = (EventEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
