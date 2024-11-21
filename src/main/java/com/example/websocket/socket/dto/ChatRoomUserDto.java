package com.example.websocket.socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomUserDto {

    private Long roomId;
    private Long userId;
}
