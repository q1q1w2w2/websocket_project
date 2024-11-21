package com.example.websocket.socket.repository;

import com.example.websocket.socket.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN UserChatRoom ucr ON cr.id = ucr.chatRoom.id WHERE ucr.user.loginId = :loginId AND cr.deleted = false")
    List<ChatRoom> findByUserLoginId(@Param("loginId") String loginId);

    Optional<ChatRoom> findByUuid(String uuid);
}
