package com.example.websocket.service.user;

import com.example.websocket.domain.user.FriendShip;
import com.example.websocket.domain.user.User;
import com.example.websocket.exception.user.FriendShipException;
import com.example.websocket.repository.jpa.user.FriendShipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipRepository friendShipRepository;

    public List<FriendShip> findAllByUser(User user) {
        return friendShipRepository.findAllByUser(user);
    }

    // 친구 관계를 삭제한 유저 정보 반환(반환 메시지를 위함)
    public User deleteFriendShip(Long friendShipId, User user) {
        FriendShip friendShip = friendShipRepository.findById(friendShipId)
                .orElseThrow(() -> new FriendShipException("존재하지 않는 관계입니다."));

        // 역방향도 삭제
        friendShipRepository.findByUserAndFriend(friendShip.getFriend(), user)
                .ifPresent(friendShipRepository::delete);
        friendShipRepository.delete(friendShip);

        return friendShip.getFriend();
    }
}
