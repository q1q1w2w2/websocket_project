package com.example.websocket.socket.web;

import com.example.websocket.socket.domain.ChatRoom;
import com.example.websocket.socket.domain.Message;
import com.example.websocket.socket.domain.UserChatRoom;
import com.example.websocket.socket.dto.ChatMessageDto;
import com.example.websocket.socket.dto.ChatMessageResponseDto;
import com.example.websocket.socket.dto.CreateRoomDto;
import com.example.websocket.socket.dto.JoinRoomDto;
import com.example.websocket.socket.service.ChatRoomService;
import com.example.websocket.socket.service.MessageService;
import com.example.websocket.socket.service.UserChatRoomService;
import com.example.websocket.user.domain.User;
import com.example.websocket.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.websocket.token.JwtFilter.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final UserChatRoomService userChatRoomService;

    @GetMapping("/chat")
    public String getChatPage(@RequestParam(required = false) String id, Model model) {
        // 파라미터가 없으면 로그인 페이지로 리다이렉트
        if (id == null || id.isEmpty()) {
            return "redirect:/login";
        }

        ChatRoom room = chatRoomService.findByUuid(id);
        model.addAttribute("room", room);
        return "chat"; // 채팅 페이지로 이동
    }

    // 채팅방 목록(로그인 성공 시 여기로 이동됨)
    @GetMapping("/chat/list")
    public String getChatListPage(@RequestParam(required = false) String accessToken, Model model) throws Exception {
        if (accessToken == null || accessToken.isEmpty()) {
            // accessToken이 없는 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByToken(accessToken);
            List<ChatRoom> chatRooms = chatRoomService.findByUserLoginId(user.getLoginId());
            log.info("chat rooms: {}", chatRooms);
            model.addAttribute("chatRooms", chatRooms);
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/login";
        }

        return "chatRoomList";
    }

    // 클라이언트가 /app/chat로 메시지 보낼 때 호출
    @MessageMapping("/chat")
    public void sendMessage(@Valid ChatMessageDto dto) throws Exception {

        User user = userService.getUserByToken(dto.getToken());

        Message message = messageService.save(dto, user.getId());

        // /topic/roomId 구독중인 사용자들에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/" + dto.getChatRoomId(), message);
    }

    // 새로운 방 생성하기
    @PostMapping("/chat/room/create")
    public ResponseEntity createRoom(@RequestBody CreateRoomDto dto, @RequestHeader(AUTHORIZATION_HEADER) String authorization) throws Exception {
        String token = authorization.substring(BEARER_PREFIX.length());
        User user = userService.getUserByToken(token);

        ChatRoom room = chatRoomService.save(dto, user);
        return ResponseEntity.ok(Map.of("message", "[" + room.getRoomName() + "]이 생성되었습니다."));
    }

    // 채팅방에 참가하기(db상으로 참가만 시킴)
    @PostMapping("/chat/join")
    public ResponseEntity joinRoom(@RequestBody JoinRoomDto dto, @RequestHeader(AUTHORIZATION_HEADER) String authorization) throws Exception {
        String token = authorization.substring(BEARER_PREFIX.length());
        User user = userService.getUserByToken(token);
        ChatRoom room = chatRoomService.findByUuid(dto.getRoomId());

        // userId와 roomId를 가진 UserChatRoom을 찾고 없으면 생성
        if (!userChatRoomService.findByUserAndChatRoom(user, room)) {
            userChatRoomService.joinChatRoom(new UserChatRoom(user, room));
        }
        return ResponseEntity.ok(Map.of("message", "채팅방에 참가했습니다."));
    }

    // 채팅방 입장 시 해당 방에서 대화한 기록 불러오기
    @GetMapping("/chat/message")
    public ResponseEntity getMessage(@RequestParam String id) {

        ChatRoom room = chatRoomService.findByUuid(id);
        List<Message> messageList = messageService.findByChatRoomId(room.getId());

        ArrayList<ChatMessageResponseDto> response = new ArrayList<>();
        for (Message message : messageList) {
            // todo n+1문제 발생할것임 -> 메시지 리스트 불러올 때 같이 불러오도록 수정 -> 같은 데이터면 쿼리 안나가는 것 같은데 그럼 상관없긴함
            User messageUser = userService.findById(message.getSenderId());
            ChatMessageResponseDto dto = new ChatMessageResponseDto(message, messageUser);
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }
}
