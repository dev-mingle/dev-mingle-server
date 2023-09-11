package com.example.dm.chat.service;

import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.repository.ChatRoomRepository;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createRoom(ChatRoomDto chatRoomDto) {
        ChatRoom room = ChatRoom.builder()
                .name(chatRoomDto.getName())
                .build();

        return chatRoomRepository.save(room);
    }

    public ChatRoom findRoomById(Long roomId) {
        return verifyRoom(roomId);
    }

    private ChatRoom verifyRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ApiResultStatus.ROOM_NOT_FOUND));
    }

    // todo: user 기반 room 조회

}
