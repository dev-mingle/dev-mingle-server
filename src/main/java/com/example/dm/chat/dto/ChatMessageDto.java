package com.example.dm.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDto {

    private String id;

    private String sender;

    private String message;

    // todo: date format 수정
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
