package com.example.websocket.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "friendship")
@NoArgsConstructor
public class FriendShip { // 실제 친구 관계

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    private LocalDateTime createdAt;

    @Builder
    public FriendShip(User user, User friend) {
        this.user = user;
        this.friend = friend;
        this.createdAt = LocalDateTime.now();
    }
}
