package com.example.websocket.service.user;

import com.example.websocket.dto.auth.JoinDto;
import com.example.websocket.token.TokenProvider;
import com.example.websocket.domain.user.User;
import com.example.websocket.exception.user.UserAlreadyExistException;
import com.example.websocket.exception.user.UserNotFoundException;
import com.example.websocket.repository.jpa.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        // 아이디 중복 검증
        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new UserAlreadyExistException(); // 기본 메시지 반환됨
        }
        // username 중복 검증
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("이미 존재하는 사용자명입니다.");
        }

        User user = User.builder()
                .nickname(dto.getUsername())
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
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