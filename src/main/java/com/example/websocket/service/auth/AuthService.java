package com.example.websocket.service.auth;

import com.example.websocket.token.TokenProvider;
import com.example.websocket.dto.auth.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public Map<String, String> login(LoginDto dto) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // authenticationManger 내부적으로 UserDetailsService 호출
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String subject = authentication.getName();
        String authority = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("refreshToken", tokenProvider.createRefreshToken(subject));
        tokens.put("accessToken", tokenProvider.createAccessToken(subject, authority));
        return tokens;
    }

    @Transactional
    public void logout(String refreshToken, String accessToken) throws Exception {
        if (!tokenProvider.validateToken(accessToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("토큰이 유효하지 않습니다.");
        }
//        refreshTokenService.expiredRefreshToken(refreshToken);

        String subject = tokenProvider.extractUserIdFromRefreshToken(refreshToken);
        refreshTokenService.deleteRefreshToken(subject);

        log.info("로그아웃 성공");
    }
    /**
     * ** login 동작 **
     * Dto로 loginId, password를 받음
     * 받은 정보로 UsernamePasswordAuthenticationToken 생성
     * authenticationManager로 위의 token을 호출하여 인증 시도
     * authenticationManager는 내부적으로 UserDetailsService의 loadUserByUsername 호출하여 정보 조회
     * 사용자가 존재하면 UserDetails 반환, 없다면 UsernameNotFoundException
     * 요청으로 받은 password와 UserDetails의 password 비교(BadCredentialException)
     * 성공 시 Authentication 객체 반환
     * Authentication객체를 SecurityContext에 저장하고 jwt 생성
     */

}
