package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceEntityRepository extends JpaRepository<PlaceEntity, Long> {
}
