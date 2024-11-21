package com.example.websocket.socket.service;

import com.example.websocket.socket.domain.ChatRoom;
import com.example.websocket.socket.domain.UserChatRoom;
import com.example.websocket.socket.dto.CreateRoomDto;
import com.example.websocket.socket.exception.ChatRoomNotFoundException;
import com.example.websocket.socket.repository.ChatRoomRepository;
import com.example.websocket.socket.repository.UserChatRoomRepository;
import com.example.websocket.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    public ChatRoom save(CreateRoomDto dto, User user) {
        String uuid = UUID.randomUUID().toString();
        ChatRoom saveRoom = new ChatRoom(dto.getRoomName(), uuid);

        ChatRoom room = chatRoomRepository.save(saveRoom);
        userChatRoomRepository.save(new UserChatRoom(user, room));
        return room;
    }

    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
    }

    public List<ChatRoom> findByUserLoginId(String loginId) {
        return chatRoomRepository.findByUserLoginId(loginId);
    }

    public ChatRoom findByUuid(String uuid) {
        return chatRoomRepository.findByUuid(uuid)
                .orElseThrow(ChatRoomNotFoundException::new);
    }
}
