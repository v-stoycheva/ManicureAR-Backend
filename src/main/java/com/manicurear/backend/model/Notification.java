package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    private String type; // 'REMINDER', 'CONFIRMED' и т.н.

    private Boolean isRead = false;

    private OffsetDateTime sentAt = OffsetDateTime.now();
}