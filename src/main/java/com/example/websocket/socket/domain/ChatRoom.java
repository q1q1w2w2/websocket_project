package com.example.websocket.socket.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private Set<UserChatRoom> chatRooms = new HashSet<>();

    public ChatRoom(String roomName, String uuid) {
        this.roomName = roomName;
        this.uuid = uuid;
        this.createdAt = LocalDateTime.now();
    }

    // ===================================

    public void delete() {
        this.deleted = true;
    }
}
