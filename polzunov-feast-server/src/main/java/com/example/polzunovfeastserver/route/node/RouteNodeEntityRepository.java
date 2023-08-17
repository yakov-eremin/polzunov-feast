package com.example.polzunovfeastserver.route.node;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteNodeEntityRepository extends JpaRepository<RouteNodeEntity, Long> {
    boolean existsByEventId(Long eventId);
}
