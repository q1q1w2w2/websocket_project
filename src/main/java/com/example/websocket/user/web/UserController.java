package com.example.websocket.user.web;

import com.example.websocket.auth.dto.JoinDto;
import com.example.websocket.token.JwtFilter;
import com.example.websocket.user.domain.User;
import com.example.websocket.user.dto.UserResponseDto;
import com.example.websocket.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static com.example.websocket.token.JwtFilter.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/api/join")
    public ResponseEntity<Map<String, Object>> join(@Validated @RequestBody JoinDto dto) {
        User user = userService.join(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원가입이 완료되었습니다.");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    // 사용자 정보
    @GetMapping("/user/info")
    public ResponseEntity<UserResponseDto> userInfo(@RequestHeader(AUTHORIZATION_HEADER) String authorization) throws Exception {
        User user = userService.getUserByToken(authorization.substring(BEARER_PREFIX.length()));
        UserResponseDto response = new UserResponseDto(user);
        return ResponseEntity.ok(response);
    }
}
