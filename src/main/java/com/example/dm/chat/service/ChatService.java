package com.example.dm.chat.service;

import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
