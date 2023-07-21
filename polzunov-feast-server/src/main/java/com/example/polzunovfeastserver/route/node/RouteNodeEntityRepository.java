package com.example.polzunovfeastserver.route.node;

import com.example.polzunovfeastserver.route.node.entity.RouteNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteNodeEntityRepository extends JpaRepository<RouteNodeEntity, Long> {
    boolean existsByEventId(Long eventId);
}
