package com.example.polzunovfeastserver.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTokenEntityRepository extends JpaRepository<NotificationTokenEntity, Long> {
    void deleteByTokenAndUser_id(String token, long userId);

    boolean existsByToken(String token);

    boolean existsByTokenAndUser_id(String token, long userId);
}
