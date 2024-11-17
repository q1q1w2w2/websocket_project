package com.example.websocket.gpt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GptRequest {

    private String model;
    private List<Message> messages;
    private int temperature;
    private int maxTokens;
    private int topP;
    private int frequencyPenalty;
    private int presencePenalty;

    public GptRequest(String model
            , String prompt
            , int temperature
            , int maxTokens
            , int topP
            , int frequencyPenalty
            , int presencePenalty) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
        this.temperature = temperature;
        this.maxTokens = maxTokens; // 응답 받을 최대 토큰 수(글자 제한)
        this.topP = topP;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;

    }
}
