package com.example.websocket.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JoinDto {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Size(min = 2, max = 20, message = "사용자 이름의 길이는 2에서 20 사이어야 합니다.")
    private String username;

    @NotEmpty(message = "아이디는 비어있을 수 없습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    @Size(min = 3, max = 100, message = "비밀번호의 길이는 3에서 100 사이어야 합니다.")
    private String password;

}
