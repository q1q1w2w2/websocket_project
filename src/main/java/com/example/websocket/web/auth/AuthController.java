package com.example.websocket.web.auth;

import com.example.websocket.dto.auth.LoginDto;
import com.example.websocket.dto.auth.RefreshTokenDto;
import com.example.websocket.service.auth.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.Map;

import static com.example.websocket.token.JwtFilter.*;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 로그인
    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody LoginDto dto, HttpSession session) throws Exception {
        Map<String, String> token = authService.login(dto);
        String accessToken = token.get("accessToken");
        String refreshToken = token.get("refreshToken");

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        // todo jwt와 session 복합 사용. 좀 더 알아보기
        session.setAttribute("accessToken", accessToken);

        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/api/logout")
    public ResponseEntity<Map> logout(
            @RequestHeader(AUTHORIZATION_HEADER) String authorization,
            @Validated @RequestBody RefreshTokenDto dto,
            HttpSession session
    ) throws Exception {
        String refreshToken = dto.getRefreshToken();
        String accessToken = authorization.substring(BEARER_PREFIX.length());

        authService.logout(refreshToken, accessToken);

        session.removeAttribute("accessToken");
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }
}
