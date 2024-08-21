package com.example.polzunovfeastserver.notification;

import com.example.polzunovfeastserver.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationTokenEntityRepository tokenRepo;

    public void deleteTokenByUserId(long id, String token) {
        if (!tokenRepo.existsByToken(token)) {
            return;
        }
        tokenRepo.deleteByTokenAndUser_id(token, id);
    }

    /**
     * Adds new notification token to user. One user cannot have the same token twice.
     *
     * @param user token will be associated with this user
     * @return optional of empty if user already has this token, new token otherwise
     */
    public Optional<NotificationTokenEntity> addNotificationToken(UserEntity user, String token) {
        if (tokenRepo.existsByTokenAndUser_id(token, user.getId())) {
            log.info("Cannot add token '{}' for user with id={}, because user already has this token", token, user.getId());
            return Optional.empty();
        }
        NotificationTokenEntity tokenEntity = new NotificationTokenEntity(null, token, user);
        tokenRepo.save(tokenEntity);
        return Optional.of(tokenEntity);
    }
}
