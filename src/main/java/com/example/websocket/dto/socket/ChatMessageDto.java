package com.example.websocket.dto.socket;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessageDto {

    // 메시지 타입(입장, 채팅)
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 방 번호
    private String token; // 채팅 보낸 사용자의 토큰
    private String username; // 사용자 이름

    @NotBlank(message = "메시지를 입력해주세요.")
    private String message; // 메시지

}
