package com.example.websocket.dto.socket;

import com.example.websocket.domain.socket.ChatRoom;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponseDto {

    private Long id;
    private String roomName;
    private String uuid;
    private boolean deleted;
    private LocalDateTime createdAt;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomName = chatRoom.getRoomName();
        this.uuid = chatRoom.getUuid();
        this.deleted = chatRoom.isDeleted();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
