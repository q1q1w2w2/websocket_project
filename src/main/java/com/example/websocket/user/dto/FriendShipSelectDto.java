package com.example.websocket.user.dto;

import com.example.websocket.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendShipSelectDto {

    private Long userId;
    private String username;

    public FriendShipSelectDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }
}
