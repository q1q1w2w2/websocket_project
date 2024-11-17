package com.example.websocket.user.dto;

import com.example.websocket.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String loginId;
    private String username;

    public UserResponseDto(User user) {
        this.loginId = user.getLoginId();
        this.username = user.getNickname();
    }
}
