package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {
}
