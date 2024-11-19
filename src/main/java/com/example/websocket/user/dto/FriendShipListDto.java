package com.example.websocket.user.dto;

import com.example.websocket.user.domain.FriendShip;
import com.example.websocket.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendShipListDto {

    private Long friendshipId;
    private String username;
    private String loginId;
    private LocalDateTime createdAt;

    public FriendShipListDto(FriendShip friendship) {
        this.friendshipId = friendship.getId();
        this.username = friendship.getFriend().getUsername();
        this.loginId = friendship.getFriend().getLoginId();
        this.createdAt = friendship.getCreatedAt();
    }
}
