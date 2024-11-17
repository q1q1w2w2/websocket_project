package com.example.websocket.exception;

import com.example.websocket.exception.dto.ExceptionResponseDto;
import com.example.websocket.socket.exception.ChatRoomNotFoundException;
import com.example.websocket.user.exception.UserAlreadyExistException;
import com.example.websocket.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return createErrorResponse(e, CONFLICT, "이미 존재하는 계정입니다.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        return createErrorResponse(e, NOT_FOUND, "사용자를 찾을 수 없습니다.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException(BadCredentialsException e) {
        return createErrorResponse(e, UNAUTHORIZED, "아이디 또는 비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
        return createErrorResponse(e, UNAUTHORIZED, "채팅방을 찾을 수 없습니다.");
    }

    private ResponseEntity createErrorResponse(Exception e, HttpStatus status, String message) {
        log.error("[{} 발생]", e.getClass().getSimpleName());
        ExceptionResponseDto response = new ExceptionResponseDto(status.value(), status.name(), message);
        return ResponseEntity.status(status).body(response);
    }

}