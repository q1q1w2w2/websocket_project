package com.example.websocket.dto.socket;

import com.example.websocket.domain.socket.Message;
import com.example.websocket.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageResponseDto {

    private Long senderId;
    private String username;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessageResponseDto(Message message, User user) {
        this.senderId = message.getSenderId();
        this.username = user.getUsername();
        this.message = message.getMessage();
        this.timestamp = message.getTimestamp();
    }
}
