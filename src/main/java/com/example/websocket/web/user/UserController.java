package com.example.websocket.web.user;

import com.example.websocket.dto.auth.JoinDto;
import com.example.websocket.domain.user.User;
import com.example.websocket.dto.user.UserResponseDto;
import com.example.websocket.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
