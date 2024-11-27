package com.example.websocket.web.gpt;

import com.example.websocket.dto.gpt.GptRequest;
import com.example.websocket.dto.gpt.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
@Slf4j
public class GptController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        log.info("prompt: {}", prompt);

        // GptRequest -> gpt에 질문할 때 이런 형식에 맞춰 요청해야 함
        GptRequest request = new GptRequest(
                model,
//                "채팅으로 대화 중이야. 대화 중 \'"+ prompt + "\'라는 답장을 받았을 때 어떻게 답장하는 것이 좋을지에 대해 실제 친구처럼 짧게 답변해줘.",
                "채팅으로 대화 중이야. 최근 대화를 보여줄게. " + prompt + " 이 대화 맥락에서 다음에 어떤 답장을 하는 것이 좋을지에 대해 실제 친구처럼 짧게 답변해줘." ,
                1, 64, 1, 2, 2
        );
        // gpt에 내가 만든 GptRequest Post 요청을 보내고 GptResponse 형식에 맞춰 응답받음(이걸 위해 restTemplate 사용)
        GptResponse gptResponse = restTemplate.postForObject(apiUrl, request, GptResponse.class);

        log.info("request: {}", request);
        log.info("gptResponse: {}", gptResponse);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
