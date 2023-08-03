package com.example.polzunovfeastserver.place.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Table(name = "places")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "place_name")
    private String name;

    @Size(max = 512)
    private String address;
}
