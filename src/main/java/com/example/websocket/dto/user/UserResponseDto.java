package com.example.websocket.dto.user;

import com.example.websocket.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long userId;
    private String loginId;
    private String username;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.loginId = user.getLoginId();
        this.username = user.getUsername();
    }
}
