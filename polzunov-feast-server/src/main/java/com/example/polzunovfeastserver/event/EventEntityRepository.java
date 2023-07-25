package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventEntityRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    @Query("select e from EventEntity e where e.id in :ids order by e.startTime asc ")
    List<EventEntity> findAllByIdOrderByStartTimeAsc(List<Long> ids);

    boolean existsByPlace_Id(Long placeId);
}
