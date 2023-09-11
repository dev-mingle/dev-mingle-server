package com.example.dm.chat.controller;

import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ChatRoom createRoom(String name) {
        return chatRoomService.createRoom(name);
    }
}
