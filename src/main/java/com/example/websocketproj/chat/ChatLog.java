package com.example.websocketproj.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
