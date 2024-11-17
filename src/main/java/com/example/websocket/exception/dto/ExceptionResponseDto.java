package com.example.websocket.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionResponseDto {

    private final int code; // 에러코드
    private final String error; // 에러명
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
