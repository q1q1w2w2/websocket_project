package com.example.websocket.dto.user;

import com.example.websocket.domain.user.FriendShip;
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
