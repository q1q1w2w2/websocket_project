package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 엔드포인트 등록
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        // webSocket을 사용할 수 없는 브라우저라면 다른 방식 선택
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메세지 브로커 설정
        config.enableSimpleBroker("/topic"); // 클라이언트가 구독할 수 있는 주제(/topic 으로 시작하는 경로에 대해 브로거 활성화, /topic/room1 을 구독하면 이 경로로 오는 모든 메시지 수신)
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 보낼 때 사용할 접두사(ex /app/send로 보내면 컨트롤러에서 처리함)
    }

}