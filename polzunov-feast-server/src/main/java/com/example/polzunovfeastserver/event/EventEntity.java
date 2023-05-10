package com.example.polzunovfeastserver.event;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "events")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "place_id")
    private final Long id;

    @Column(name = "event_name")
    private final String name;

    @Column(name = "start_date_time")
    private final LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private final LocalDateTime endDateTime;

    @OneToOne
    private final PlaceEntity place;
}
