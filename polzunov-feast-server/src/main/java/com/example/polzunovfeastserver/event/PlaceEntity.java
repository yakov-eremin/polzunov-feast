package com.example.polzunovfeastserver.event;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "places")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "place_id")
    private final Long id;

    @Column(name = "place_name")
    private final String name;

    private final String address;
    private final Double latitude;
    private final Double longitude;
}
