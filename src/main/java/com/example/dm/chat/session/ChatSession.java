package com.example.dm.chat.session;

import com.example.dm.chat.dto.ChatRoomDto;
import com.example.dm.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatSession {

    private ObjectMapper mapper = new ObjectMapper();
    private Set<WebSocketSession> sessions = new HashSet<>();

    public void handleSession(WebSocketSession session, ChatRoomDto message) throws IOException{

        // todo: 입장문 Message DB에 저장?
        if (message.getType().equals(ChatRoomDto.MessageType.ENTER)) {
            sessions.add(session);

            message.setMessage(message.getSender() + "님이 입장하셨습니다");
        }

        if (message.getType().equals(ChatRoomDto.MessageType.SEND)) {
            message.setMessage(message.getMessage());
        }

        sendMessage(message);

    }

    public <T> void sendMessage(T message) throws IOException {
        TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
        sessions.parallelStream().forEach(session -> {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        });
    }
}
