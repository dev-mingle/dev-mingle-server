package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    @MessageMapping("/chat/enter")
    public void enterUser(@Payload ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());

        chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }

    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatDto chatDto) {
        template.convertAndSend("/sub/chat/room/"+chatDto.getRoomId(), chatDto);
    }
}
