package com.example.websocket.repository.jpa.socket;

import com.example.websocket.domain.socket.ChatRoom;
import com.example.websocket.domain.socket.UserChatRoom;
import com.example.websocket.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);
    List<UserChatRoom> findByChatRoom(ChatRoom chatRoom);
}
