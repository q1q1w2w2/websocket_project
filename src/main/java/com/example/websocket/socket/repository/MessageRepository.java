package com.example.websocket.socket.repository;

import com.example.websocket.socket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.chatRoomId = :chatRoomId order by m.timestamp")
    List<Message> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
