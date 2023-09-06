package com.example.dm.chat.service;

import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.repository.ChatRoomRepository;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createRoom(String name) {
        ChatRoom room = ChatRoom.builder()
                .name(name)
                .build();

        return chatRoomRepository.save(room);
    }

    public ChatRoom findRoomById(Long roomId) {
        return verifyRoom(roomId);
    }

    private ChatRoom verifyRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

}
