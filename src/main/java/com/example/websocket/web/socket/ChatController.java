package com.example.websocket.web.socket;

import com.example.websocket.domain.socket.ChatRoom;
import com.example.websocket.domain.socket.Message;
import com.example.websocket.domain.socket.UserChatRoom;
import com.example.websocket.dto.socket.*;
import com.example.websocket.service.socket.ChatRoomService;
import com.example.websocket.service.socket.MessageService;
import com.example.websocket.service.socket.UserChatRoomService;
import com.example.websocket.domain.user.User;
import com.example.websocket.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.example.websocket.token.JwtFilter.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final UserChatRoomService userChatRoomService;

    @GetMapping("/chat")
    public String getChatPage(@RequestParam(required = false) String id, Model model, HttpSession session) throws Exception {
        // 로그인 검증
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            return "redirect:/login";
        }
        User user = userService.getUserByToken(accessToken);

        // 방 uuid 확인 + 검증도 필요함
        if (id == null || id.isEmpty()) {
            return "redirect:/login";
        }
        ChatRoom room = chatRoomService.findByUuid(id);

        // 채팅방에 속한 유저인지 확인
        if (!userChatRoomService.findByUserAndChatRoom(user, room)) {
            return "redirect:/chat/list";
        }

        // 참가자 목록
        ArrayList<User> users = new ArrayList<>();
        List<UserChatRoom> chatRooms = userChatRoomService.findByChatRoom(room);
        for (UserChatRoom chatRoom : chatRooms) {
            users.add(chatRoom.getUser());
        }
        model.addAttribute("room", room);
        model.addAttribute("users", users);

        return "chat";
    }

    // 채팅방 목록(로그인 성공 시 여기로 이동됨)
    @GetMapping("/chat/list")
    public String getChatListPage(Model model, HttpSession session) {
        try {
            String accessToken = (String) session.getAttribute("accessToken");

            if (accessToken == null || accessToken.isEmpty()) {
                return "redirect:/login";
            }

            User user = userService.getUserByToken(accessToken);
            List<ChatRoom> chatRooms = chatRoomService.findByUserLoginId(user.getLoginId());

            model.addAttribute("chatRooms", chatRooms);
            model.addAttribute("user", user);

            return "chatRoomList";

        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    // /topic/roomId 구독중인 사용자들에게 메시지 전송
    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/{chatRoomId}")
    public Message sendMessage(@DestinationVariable String chatRoomId, @Valid ChatMessageDto message) throws Exception {
        User user = userService.getUserByToken(message.getToken());
        return messageService.save(message, user.getId());
    }

    // 대화방 입장 시 입장메시지
    @MessageMapping("/chat/enter")
    public void enterMessage(@Valid ChatMessageDto message) throws Exception {
        sendStateMessage(message, "님이 채팅방에 입장했습니다.");
    }

    // 대화방 초대 시 입장메시지
    @MessageMapping("/chat/invite")
    public void inviteMessage(@Valid ChatMessageDto message) throws Exception {
        sendStateMessage(message, "님을 채팅방에 초대했습니다.");
    }

    // 대화방 퇴장 시 퇴장메시지
    @MessageMapping("/chat/exit")
    public void leaveMessage(@Valid ChatMessageDto message) throws Exception {
        sendStateMessage(message, "님이 채팅방에서 퇴장했습니다.");
    }

    // 상황별 메시지 출력 메서드
    private void sendStateMessage(ChatMessageDto message, String stateMessage) throws Exception {
        User user = userService.getUserByToken(message.getToken());
        message.setMessage(message.getUsername() + stateMessage);
        message.setUsername(user.getUsername());
        Message saveMessage = messageService.save(message, user.getId());
        messagingTemplate.convertAndSend("/topic/" + message.getChatRoomId(), saveMessage);
    }

    // 새로운 방 생성하기
    @PostMapping("/chat/room/create")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody CreateRoomDto dto, @RequestHeader(AUTHORIZATION_HEADER) String authorization) throws Exception {
        String token = authorization.substring(BEARER_PREFIX.length());
        User user = userService.getUserByToken(token);

        ChatRoom room = chatRoomService.save(dto, user);
        ChatRoomResponseDto roomInfo = new ChatRoomResponseDto(room);
        return ResponseEntity.ok(
                Map.of("message", "[" + room.getRoomName() + "]이 생성되었습니다.",
                        "roomInfo", roomInfo)
        );
    }

    // 채팅방에 참가하기
    @PostMapping("/chat/join")
    public ResponseEntity<Map<String, Object>> joinRoom(@RequestBody ChatRoomUuidDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        ChatRoom chatRoom = chatRoomService.findByUuid(dto.getRoomId());

        boolean result = userChatRoomService.joinChatRoom(user, chatRoom);
        if (result) {
            ChatRoomResponseDto roomInfo = new ChatRoomResponseDto(chatRoom);
            return ResponseEntity.ok(
                    Map.of("message", "채팅방에 참가했습니다.",
                            "roomInfo", roomInfo)
            );
        }
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of("message", "이미 참가된 대화방입니다."));
    }

    // 채팅방 입장 시 해당 방에서 대화한 기록 불러오기
    @GetMapping("/chat/message")
    public ResponseEntity getMessage(@RequestParam String id) {

        ChatRoom room = chatRoomService.findByUuid(id);
        List<Message> messageList = messageService.findByChatRoomId(room.getId());

        ArrayList<ChatMessageResponseDto> response = new ArrayList<>();
        for (Message message : messageList) {
            User messageUser = userService.findById(message.getSenderId());
            ChatMessageResponseDto dto = new ChatMessageResponseDto(message, messageUser);
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    // 최근 5개의 대화를 사용해서 gpt 추천 답변을 반환
    @PostMapping("/chat/help")
    public ResponseEntity<Map<String, Object>> gptForHelp(@RequestBody ChatRoomUuidDto dto) {
        ChatRoom room = chatRoomService.findByUuid(dto.getRoomId());
        // 해당 room의 메시지 중 가장 마지막 메시지 불러오기
        List<Message> messageList = messageService.findByChatRoomId(room.getId());

        if (messageList.size() < 5) {
            throw new RuntimeException("최소 5번의 대화 이후에 이용할 수 있습니다.");
        }
        // 맥락 파악을 위해 이전 대화들 가져오기
        ArrayList<String> list = new ArrayList<>();
        for (int i = messageList.size() - 5; i < messageList.size(); i++) {
            list.add(messageList.get(i).getMessage());
        }
        String messages = String.join(", ", list);
        log.info("messages: {}", messages);

        String answer = restTemplate.getForObject("http://localhost:8082/gpt/chat?prompt=" + messages, String.class);
        log.info("answer: {}", answer);

        return ResponseEntity.ok(Map.of("answer", answer));
    }

    // 대화방 나가기
    @DeleteMapping("/chat/exit")
    public ResponseEntity<Map<String, String>> exitChatRoom(@RequestBody ChatRoomIdDto dto) {
        User user = userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        userChatRoomService.exitChatRoom(dto.getRoomId(), user);
        return ResponseEntity.ok(Map.of("message", "대화방에서 나갔습니다."));
    }

    // 대화방으로 친구 초대
    @PostMapping("/api/chat/invite")
    public ResponseEntity inviteFriendToChatRoom(@RequestBody ChatRoomUserDto dto) {
        User friend = userService.findById(dto.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(dto.getRoomId());
        boolean result = userChatRoomService.joinChatRoom(friend, chatRoom);

        if (result) {
            return ResponseEntity.ok(Map.of("message", "대화방에 [" + friend.getUsername() + "]을(를) 초대했습니다."));
        }
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of("message", "이미 대화방에 참가한 사용자입니다."));
    }
}
