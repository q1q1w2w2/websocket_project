package com.example.websocket.socket.repository;

import com.example.websocket.socket.domain.ChatRoom;
import com.example.websocket.socket.domain.UserChatRoom;
import com.example.websocket.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);
}
