package com.example.websocket.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean { //OncePer 뭐시기

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JWT 인증 정보 Security Context에 저장");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();
        log.info("jwt: {}", jwt);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = null;
            try {
                authentication = tokenProvider.getAuthentication(jwt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보 저장, uri: {}", authentication.getName(), requestURI);
        } else {
            log.info("유효한 JWT 없음, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest request) {
        log.info("header에서 토큰 꺼냄");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}