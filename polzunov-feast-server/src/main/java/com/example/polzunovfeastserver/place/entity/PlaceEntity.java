package com.example.polzunovfeastserver.place.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "places")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "place_name")
    private String name;

    private String address;
}
