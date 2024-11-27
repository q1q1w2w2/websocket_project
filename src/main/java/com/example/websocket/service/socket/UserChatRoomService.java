package com.example.websocket.service.socket;

import com.example.websocket.domain.socket.ChatRoom;
import com.example.websocket.domain.socket.UserChatRoom;
import com.example.websocket.exception.socket.ChatRoomNotFoundException;
import com.example.websocket.repository.jpa.socket.ChatRoomRepository;
import com.example.websocket.repository.jpa.socket.UserChatRoomRepository;
import com.example.websocket.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    public boolean joinChatRoom(User user, ChatRoom chatRoom) {
        // userId와 roomId를 가진 UserChatRoom을 찾고 없으면 생성(이미 참가된 상태 검증)
        if (!findByUserAndChatRoom(user, chatRoom)) {
            userChatRoomRepository.save(new UserChatRoom(user, chatRoom));
            return true;
        }
        return false;
    }

    public boolean findByUserAndChatRoom(User user, ChatRoom chatRoom) {
        return userChatRoomRepository.findByUserAndChatRoom(user, chatRoom).isPresent();
    }

    public List<UserChatRoom> findByChatRoom(ChatRoom chatRoom) {
        return userChatRoomRepository.findByChatRoom(chatRoom);
    }

    // 대화방 나가기 + todo 대화방에 사람이 1명일 때 나가기를 하면 대화방 자체도 삭제
    public void exitChatRoom(Long chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new RuntimeException("해당 대화방에 참가하지 않은 사용자입니다."));
        userChatRoomRepository.delete(userChatRoom);
    }
}
