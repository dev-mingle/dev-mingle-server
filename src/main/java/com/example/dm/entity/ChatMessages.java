package com.example.dm.entity;

import com.example.dm.dto.chats.ChatCreateDto;
import com.example.dm.dto.chats.ChatPatchDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Builder
public class ChatMessages extends BaseTimeEntity {

    @Id
    private String id;

    private Long roomId;

    private Long userProfileId;

    private String sender;

    private String message;

    private List<String> imageUrls;

    public static ChatMessages from(ChatCreateDto chatCreateDto, Long roomId) {
        return ChatMessages.builder()
                .message(chatCreateDto.getMessage())
                .userProfileId(chatCreateDto.getUserProfileId())
                .sender(chatCreateDto.getSender())
                .roomId(roomId)
                .imageUrls(chatCreateDto.getImageUrls())
                .build();
    }

    public ChatMessages updateMessage(ChatPatchDto chatPatchDto) {
        this.message = chatPatchDto.getMessage();
        return this;
    }

    public void deleteMessage() {
        this.message = "삭제된 메시지입니다.";
    }
}
