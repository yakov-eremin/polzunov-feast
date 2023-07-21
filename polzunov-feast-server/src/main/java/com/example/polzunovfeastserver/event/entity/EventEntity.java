package com.example.polzunovfeastserver.event.entity;

import com.example.polzunovfeastserver.event.util.EventTableKeys;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

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
            foreignKey = @ForeignKey(name = EventTableKeys.FOREIGN_PLACE, value = ConstraintMode.CONSTRAINT)
    )
    private PlaceEntity place;

    private boolean canceled;
}
