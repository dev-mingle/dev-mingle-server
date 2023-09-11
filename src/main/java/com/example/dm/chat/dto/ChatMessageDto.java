package com.example.dm.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageDto {

    private String id;

    private String sender;

    private String message;

    private String createdAt;

    private String updatedAt;
}
