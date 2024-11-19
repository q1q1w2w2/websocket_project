package com.example.websocket.user.repository;

import com.example.websocket.user.domain.Friend;
import com.example.websocket.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    List<Friend> findByToUserIdOrderByCreatedAtDesc(Long userId);
}
