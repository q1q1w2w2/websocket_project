package com.example.websocket.user.web;

import com.example.websocket.user.domain.Friend;
import com.example.websocket.user.domain.FriendShip;
import com.example.websocket.user.domain.User;
import com.example.websocket.user.dto.*;
import com.example.websocket.user.service.FriendService;
import com.example.websocket.user.service.FriendShipService;
import com.example.websocket.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FriendShipController {

    private final UserService userService;
    private final FriendService friendService;
    private final FriendShipService friendShipService;

    @GetMapping("/friend/list")
    public String friendList(Model model, HttpSession session) throws Exception {
        String accessToken = (String) session.getAttribute("accessToken");
        User user = userService.getUserByToken(accessToken);

        // 친구 목록
        List<FriendShipListDto> friendShipList = new ArrayList<>();
        List<FriendShip> friendShips = friendShipService.findAllByUser(user);
        for (FriendShip friendShip : friendShips) {
            friendShipList.add(new FriendShipListDto(friendShip));
        }

        // 친구 요청 목록
        List<FriendRequestListDto> friendRequestList = new ArrayList<>();
        List<Friend> friendRequest = friendService.findByToUser(user);
        for (Friend friend : friendRequest) {
            User fromUser = userService.findById(friend.getFromUserId());
            friendRequestList.add(new FriendRequestListDto(friend, fromUser));
        }

        model.addAttribute("user", user);
        model.addAttribute("friends", friendShipList);
        model.addAttribute("friendRequests", friendRequestList);

        return "friendList";
    }

    // 친구 추가 요청(닉네임으로 추가)
    @PostMapping("/friend/request")
    public ResponseEntity<Map> requestFriend(@RequestBody FriendNameRequestDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        friendService.requestFriend(user, dto.getUsername());

        return ResponseEntity.ok(Map.of("message", "[" + user.getUsername() + "]님이 [" + dto.getUsername() + "]님에게 친구요청을 보냈습니다."));
    }

    // 친구 추가 요청 수락
    @PatchMapping("/friend/request/accept")
    public ResponseEntity<Map> acceptFriend(@RequestBody FriendIdRequestDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        Friend friend = friendService.findById(dto.getFriendId());
        User fromUser = userService.findById(friend.getFromUserId()); // 응답 메시지를 위해 추가

        // 친구 요청 수락
        friendService.accept(friend, user);
        return ResponseEntity.ok(Map.of("message", fromUser.getUsername() + "님의 친구요청을 수락했습니다."));
    }

    // 친구 추가 요청 거절
    @PatchMapping("/friend/request/reject")
    public ResponseEntity<Map> rejectFriend(@RequestBody FriendIdRequestDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        Friend friend = friendService.findById(dto.getFriendId());
        User fromUser = userService.findById(friend.getFromUserId()); // 응답 메시지를 위해 추가

        // 친구 요청 거절
        friendService.reject(friend, user);
        return ResponseEntity.ok(Map.of("message", fromUser.getUsername() + "님의 친구요청을 거절했습니다."));
    }

    // 친구 삭제
    @DeleteMapping("/friend/delete")
    public ResponseEntity<Map> deleteFriend(@RequestBody FriendShipIdDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        User friend = friendShipService.deleteFriendShip(dto.getFriendshipId(), user);

        return ResponseEntity.ok(Map.of("message", friend.getUsername() + "님과의 친구 관계를 삭제했습니다."));
    }
}