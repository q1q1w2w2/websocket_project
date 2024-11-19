package com.example.websocket.socket.dto;

import com.example.websocket.socket.domain.Message;
import com.example.websocket.user.domain.User;
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
