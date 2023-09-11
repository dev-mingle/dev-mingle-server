package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatDto;
import com.example.dm.chat.service.ChatMessageService;
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

    private final ChatMessageService chatMessageService;

    private static final String SUBSCRIBE_URL = "/sub/v1/chats/";

    // todo: pathvariable로 roomId 받는 경우
    // todo: 각 room이 존재하는지 유효성, user가 채팅방에 속해있는지 유효
    @MessageMapping("/v1/chats/join")
    public void enterUser(@Payload ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());

        chatDto.setMessage(chatDto.getSender() + " 님이 입장하셨습니다.");
        processMessage(chatDto);
    }

    @MessageMapping("/v1/chats/message")
    public void sendMessage(@Payload ChatDto chatDto) {
        processMessage(chatDto);
    }

    private void processMessage(ChatDto chatDto) {
        template.convertAndSend(SUBSCRIBE_URL + chatDto.getRoomId(), chatDto);
        chatMessageService.saveMessage(chatDto);
    }
}
