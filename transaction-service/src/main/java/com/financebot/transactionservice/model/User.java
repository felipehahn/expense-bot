package com.financebot.transactionservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public User(Long id) {
        this.id = id;
    }

    public static User create(Long telegramId) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

}
