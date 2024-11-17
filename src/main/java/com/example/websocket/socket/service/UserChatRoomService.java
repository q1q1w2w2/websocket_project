package com.example.websocket.socket.service;

import com.example.websocket.socket.domain.ChatRoom;
import com.example.websocket.socket.domain.UserChatRoom;
import com.example.websocket.socket.repository.UserChatRoomRepository;
import com.example.websocket.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserChatRoomService {

    private final UserChatRoomRepository userChatRoomRepository;

    public void joinChatRoom(UserChatRoom userChatRoom) {
        userChatRoomRepository.save(userChatRoom);
    }

    public boolean findByUserAndChatRoom(User user, ChatRoom chatRoom) {
        return userChatRoomRepository.findByUserAndChatRoom(user, chatRoom).isPresent();
    }
}
