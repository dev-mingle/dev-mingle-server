package com.example.dm.chat.controller;

import com.example.dm.chat.dto.ChatCreateDto;
import com.example.dm.chat.dto.ChatMessageGetDto;
import com.example.dm.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations template;

    private final ChatMessageService chatMessageService;

    public static final String SUBSCRIBE_URL = "/sub/v1/chats/";

    @MessageMapping("/v1/chats/join")
    public void enterUser(@Payload ChatCreateDto chatCreateDto, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("roomId", chatCreateDto.getRoomId());

        chatCreateDto.setMessage(chatCreateDto.getSender() + " 님이 입장하셨습니다.");
        processMessage(chatCreateDto);
    }

    @MessageMapping("/v1/chats/message")
    public void sendMessage(@Payload ChatCreateDto chatCreateDto) {
        processMessage(chatCreateDto);
    }

    @MessageMapping("/v1/chats/exit")
    public void exitUser(@Payload ChatCreateDto chatCreateDto) {
        chatCreateDto.setMessage(chatCreateDto.getSender() + " 님이 퇴장하셨습니다.");
        processMessage(chatCreateDto);
    }

    private void processMessage(ChatCreateDto chatCreateDto) {
        ChatMessageGetDto chatMessageGetDto = chatMessageService.saveMessage(chatCreateDto);
        template.convertAndSend(SUBSCRIBE_URL + chatCreateDto.getRoomId(), chatMessageGetDto);

    }
}
