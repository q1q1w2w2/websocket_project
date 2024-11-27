package com.example.websocket.domain.socket;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
//@Table(name = "messages")
@Document(collection = "messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "username")
    private String username;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "chat_room_id")
//    private Long chatRoomId;
//
//    @Column(name = "sender_id")
//    private Long senderId;
//
//    @Column(name = "username")
//    private String username;
//
//    @Column(name = "message")
//    private String message;
//
//    @Column(name = "timestamp")
//    private LocalDateTime timestamp;


}
