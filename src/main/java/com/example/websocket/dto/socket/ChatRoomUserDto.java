package com.example.websocket.dto.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomUserDto {

    private Long roomId;
    private Long userId;
}
