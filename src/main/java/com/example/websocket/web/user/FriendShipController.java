package com.example.websocket.web.user;

import com.example.websocket.domain.user.Friend;
import com.example.websocket.domain.user.FriendShip;
import com.example.websocket.domain.user.User;
import com.example.websocket.dto.user.*;
import com.example.websocket.service.user.FriendService;
import com.example.websocket.service.user.FriendShipService;
import com.example.websocket.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FriendShipController {

    private final UserService userService;
    private final FriendService friendService;
    private final FriendShipService friendShipService;

    // 친구 목록 페이지
    @GetMapping("/friend/list")
    public String friendListPage(Model model, HttpSession session) throws Exception {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByToken(accessToken);

        // 친구 목록 todo n+1, 각각 친구(user) 정보를 가져오기 위해 쿼리 또 나감
        List<FriendShipListDto> friendShipList = new ArrayList<>();
        List<FriendShip> friendShips = friendShipService.findAllByUser(user);
        for (FriendShip friendShip : friendShips) {
            friendShipList.add(new FriendShipListDto(friendShip));
        }

        // 친구 요청 목록 todo 여기도 n+1
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

    // 친구 목록
    @GetMapping("/api/friend/list")
    public ResponseEntity friendList() {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        List<FriendShip> friendShipList = friendShipService.findAllByUser(user);

        ArrayList<FriendShipSelectDto> response = new ArrayList<>();
        for (FriendShip friendShip : friendShipList) {
            User friend = friendShip.getFriend(); // username, userId 필요
            response.add(new FriendShipSelectDto(friend));
        }

        return ResponseEntity.ok(response);
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
