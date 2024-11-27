package com.example.websocket.repository.jpa.user;

import com.example.websocket.domain.user.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    List<Friend> findByToUserIdOrderByCreatedAtDesc(Long userId);
}
