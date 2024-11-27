package com.example.websocket.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "loginId", unique = true, nullable = false)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FriendShip> friendShip = new HashSet<>();

    @Builder
    public User(String nickname, String loginId, String password) {
        this.username = nickname;
        this.loginId = loginId;
        this.password = password;
        this.createdAt = LocalDateTime.now().withNano(0);
    }
}
