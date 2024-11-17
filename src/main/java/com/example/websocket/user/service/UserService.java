package com.example.websocket.user.service;

import com.example.websocket.auth.dto.JoinDto;
import com.example.websocket.token.TokenProvider;
import com.example.websocket.user.domain.User;
import com.example.websocket.user.exception.UserAlreadyExistException;
import com.example.websocket.user.exception.UserNotFoundException;
import com.example.websocket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원가입
    @Transactional
    public User join(JoinDto dto) {

        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        User user = User.builder()
                .nickname(dto.getUsername())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public User getCurrentUser(Authentication authentication) {
        return userRepository.findByLoginId(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
    }

    public User getUserByToken(String token) throws Exception {
        String userId = tokenProvider.extractUserIdFromRefreshToken(token);
        log.info("userId: {}", userId);
        return userRepository.findByLoginId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

}