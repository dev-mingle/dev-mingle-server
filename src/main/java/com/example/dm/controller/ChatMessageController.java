package com.example.dm.controller;

import com.example.dm.dto.chats.ChatCreateDto;
import com.example.dm.dto.chats.ChatMessageGetDto;
import com.example.dm.entity.LoginUser;
import com.example.dm.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations template;

    private final ChatMessageService chatMessageService;

    @Value("${api.path.default}")
    private String DEFAULT_URL;

    // todo: 보낸 회원 검증
    @MessageMapping("${api.path.default}/chats/{roomId}/message")
    public void sendMessage(@Payload @Valid ChatCreateDto chatCreateDto,
                            @DestinationVariable Long roomId,
                            @AuthenticationPrincipal LoginUser user) {
        processMessage(chatCreateDto, roomId, user);
    }

    private void processMessage(ChatCreateDto chatCreateDto, Long roomId, LoginUser user) {
        ChatMessageGetDto chatMessageGetDto = chatMessageService.saveMessage(chatCreateDto, roomId, user);
        template.convertAndSend("/sub" + DEFAULT_URL + "/chats/" + roomId, chatMessageGetDto);
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendTo("/sub/api/v1/chats/error/{roomId}")
    public StringBuilder handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                               @Header("Room-id") @DestinationVariable String roomId) {
        StringBuilder errorMessages = new StringBuilder();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessages.append(fieldError.getField()).append(":");
            errorMessages.append(fieldError.getDefaultMessage());
            errorMessages.append(" ");
        }

        return errorMessages;
    }
}
