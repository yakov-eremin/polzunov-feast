package com.example.polzunovfeastserver.notification;

import com.example.polzunovfeastserver.notification.util.NotificationTokensTableKeys;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "notification_tokens",
        uniqueConstraints = @UniqueConstraint(name = NotificationTokensTableKeys.UNIQUE_USER_TOKEN, columnNames = {"user_id", "token"})
)
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = NotificationTokensTableKeys.FOREIGN_USER)
    )
    private UserEntity user;
}
