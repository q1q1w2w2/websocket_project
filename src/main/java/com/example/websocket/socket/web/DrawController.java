package com.example.websocket.socket.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DrawController {

    @GetMapping("/draw")
    public String draw() {
        return "draw";
    }

    @MessageMapping("/draw")
    @SendTo("/topic/draw")
    public DrawData handleDraw(DrawData drawData) {
        return drawData; // 클라이언트에게 전달
    }
}

@Getter
@Setter
class DrawData {
    private int x0;
    private int y0;
    private int x1;
    private int y1;
}