package com.example.websocket.repository.mongo.socket;

import com.example.websocket.domain.socket.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{'chatRoomId' : ?0 }")
    List<Message> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
