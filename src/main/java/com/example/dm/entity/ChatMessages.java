package com.example.dm.entity;

import com.example.dm.dto.chats.ChatCreateDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
public class ChatMessages extends BaseTimeEntity {

    @Id
    private String id;

    private Long roomId;

    private String sender;

    private String message;

    public static ChatMessages from(ChatCreateDto chatCreateDto) {
        return ChatMessages.builder()
                .message(chatCreateDto.getMessage())
                .sender(chatCreateDto.getSender())
                .roomId(chatCreateDto.getRoomId())
                .build();
    }
}
