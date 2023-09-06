package com.example.dm.chat.session;

import com.example.dm.chat.dto.ChatDto;
import com.example.dm.chat.service.ChatService;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

public class ChatSession {

    private Set<WebSocketSession> sessions = new HashSet<>();

    public void handleSession(WebSocketSession session, ChatDto message, ChatService chatService) {

        // todo: 입장문 Message DB에 저장?
        if (message.getType().equals(ChatDto.MessageType.ENTER)) {
            sessions.add(session);

            message.setMessage(message.getSender() + "님이 입장하셨습니다");
        }

        if (message.getType().equals(ChatDto.MessageType.SEND)) {
            message.setMessage(message.getMessage());
        }

        sendMessage(message, chatService);

    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}
