package com.example.dm.chat.service;

import com.example.dm.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatSocketService {

    private final ObjectMapper mapper;

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
