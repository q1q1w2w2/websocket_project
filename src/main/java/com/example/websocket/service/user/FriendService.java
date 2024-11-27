package com.example.websocket.service.user;

import com.example.websocket.domain.user.Friend;
import com.example.websocket.domain.user.FriendShip;
import com.example.websocket.domain.user.User;
import com.example.websocket.exception.user.FriendShipException;
import com.example.websocket.exception.user.UserNotFoundException;
import com.example.websocket.repository.jpa.user.FriendRepository;
import com.example.websocket.repository.jpa.user.FriendShipRepository;
import com.example.websocket.repository.jpa.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final FriendShipRepository friendShipRepository;

    // 친구 요청 보내기
    public Friend requestFriend(User user, String friendUsername) {
        User toUser = userRepository.findByUsername(friendUsername)
                .orElseThrow(UserNotFoundException::new);

        if (user.equals(toUser)) {
            throw new FriendShipException("자기 자신에게 친구요청을 보낼 수 없습니다.");
        }
        if (friendRepository.findByFromUserIdAndToUserId(user.getId(), toUser.getId()).isPresent()) {
            throw new FriendShipException("요청 수락 대기중입니다.");
        }
        if (friendShipRepository.findByUserAndFriend(user, toUser).isPresent()) {
            throw new FriendShipException("이미 친구로 등록된 사용자입니다.");
        }


        log.info("[" + user.getUsername() + "]이 [" + toUser.getUsername() + "]에게 친구요청을 보냈습니다.");

        Friend friend = Friend.builder()
                .fromUserId(user.getId())
                .toUserId(toUser.getId())
                .build();
        return friendRepository.save(friend);
    }

    // 친구 요청 수락
    public void accept(Friend friend, User user) {
        if (!friend.getToUserId().equals(user.getId())) {
            throw new FriendShipException("해당 유저에게 보낸 친구요청이 아닙니다.");
        }

        // 두 유저 모두에게 friendShip 추가해야 함
        User fromUser = userRepository.findById(friend.getFromUserId())
                .orElseThrow(UserNotFoundException::new);

        // 요청받은 사용자의 FriendShip
        FriendShip friendShipForToUser = FriendShip.builder()
                .user(user)
                .friend(fromUser)
                .build();

        // 요청보낸 사용자의 FriendShip
        FriendShip friendShipForFromUser = FriendShip.builder()
                .user(fromUser)
                .friend(user)
                .build();

        friendShipRepository.save(friendShipForToUser);
        friendShipRepository.save(friendShipForFromUser);

        // 만약 양방향으로 친구 요청을 보낸다면 양쪽 모두의 friend 삭제해야 함
        Optional<Friend> reverseFriend = friendRepository.findByFromUserIdAndToUserId(friend.getToUserId(), friend.getFromUserId());
        if (reverseFriend.isPresent()) {
            friendRepository.delete(reverseFriend.get());
        }
        // friend 삭제
        friendRepository.delete(friend);

    }

    // 친구 요청 거절
    public void reject(Friend friend, User user) {
        if (!friend.getToUserId().equals(user.getId())) {
            throw new FriendShipException("해당 유저에게 보낸 친구요청이 아닙니다.");
        }
        friendRepository.delete(friend);
    }

    // 친구 요청 찾기
    public Friend findById(Long id) {
        return friendRepository.findById(id)
                .orElseThrow(() -> new FriendShipException("해당 친구 요청을 찾을 수 없습니다."));
    }

    public List<Friend> findByToUser(User user) {
        return friendRepository.findByToUserIdOrderByCreatedAtDesc(user.getId());
    }

}
