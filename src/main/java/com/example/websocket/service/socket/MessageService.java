package com.example.websocket.service.socket;

import com.example.websocket.domain.socket.Message;
import com.example.websocket.dto.socket.ChatMessageDto;
import com.example.websocket.repository.mongo.socket.MessageRepository;
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
