package com.example.websocket.socket.service;

import com.example.websocket.socket.domain.Message;
import com.example.websocket.socket.dto.ChatMessageDto;
import com.example.websocket.socket.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message save(ChatMessageDto dto, Long userId) {
        Message message = Message.builder()
                .chatRoomId(dto.getChatRoomId())
                .senderId(userId)
                .username(dto.getUsername())
                .message(dto.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    // 방에 맞는 메시지 불러오기
    public List<Message> findByChatRoomId(Long chatRoomId) {
        return messageRepository.findAllByChatRoomId(chatRoomId);
    }
}
