package com.example.dm.chat.handler;

import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.chat.entity.ChatRoom;
import com.example.dm.chat.service.ChatRoomService;
import com.example.dm.chat.session.ChatRoomStorage;
import com.example.dm.chat.session.ChatSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final ChatRoomService chatRoomService;
    private final ChatRoomStorage chatRoomStorage;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();

        ChatRoomDto chatRoomDto = mapper.readValue(payload, ChatRoomDto.class);
        ChatRoom room = chatRoomService.findRoomById(chatRoomDto.getRoomId());

        ChatSession chatSession = chatRoomStorage.joinChat(session, room.getRoomId());
        chatSession.handleSession(session, chatRoomDto);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session + "접속");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session + "해제");
    }
}
