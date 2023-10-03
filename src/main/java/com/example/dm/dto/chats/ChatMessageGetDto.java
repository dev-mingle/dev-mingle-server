package com.example.dm.dto.chats;

import com.example.dm.entity.ChatMessages;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatMessageGetDto {

    private String id;

    private String sender;

    private String message;

    private List<String> imageUrls;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ChatMessageGetDto from(ChatMessages chatMessages) {
        return ChatMessageGetDto.builder()
                .id(chatMessages.getId())
                .sender(chatMessages.getSender())
                .message(chatMessages.getMessage())
                .imageUrls(chatMessages.getImageUrls())
                .createdAt(chatMessages.getCreatedAt())
                .updatedAt(chatMessages.getUpdatedAt())
                .build();
    }
}
