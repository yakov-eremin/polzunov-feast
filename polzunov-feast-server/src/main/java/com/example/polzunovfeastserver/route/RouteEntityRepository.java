package com.example.polzunovfeastserver.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteEntityRepository extends JpaRepository<RouteEntity, Long> {

    Optional<RouteEntity> findByOwner_Id(Long ownerId);
}
