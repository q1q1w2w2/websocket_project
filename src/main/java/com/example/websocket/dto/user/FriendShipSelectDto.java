package com.example.websocket.dto.user;

import com.example.websocket.domain.user.User;
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
