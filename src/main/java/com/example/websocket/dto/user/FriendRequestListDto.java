package com.example.websocket.dto.user;

import com.example.websocket.domain.user.Friend;
import com.example.websocket.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendRequestListDto {

    private Long id;
    private String username;
    private LocalDateTime createdAt;

    public FriendRequestListDto(Friend friend, User fromUser) {
        this.id = friend.getId();
        this.username = fromUser.getUsername();
        this.createdAt = fromUser.getCreatedAt();
    }
}
