package com.example.dm.chat.session;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ChatRoomStorage {

    private Map<Long, ChatSession> rooms;

    @PostConstruct
    private void init() {
        rooms = new LinkedHashMap<>();
    }

    public ChatSession joinChat(WebSocketSession session, Long roomId){
        ChatSession room = rooms.get(roomId);

        if (room == null) {
            // todo: refactor 필요
            rooms.put(roomId, new ChatSession());
        }

        return rooms.get(roomId);
    }
}
