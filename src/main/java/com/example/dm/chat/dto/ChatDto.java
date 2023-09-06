package com.example.dm.chat.dto;

import lombok.Getter;

@Getter
public class ChatDto {

    private String roomId;

    private String sender;

    private String message;

    private String createdAt;

    private MessageType type;

    private enum MessageType{
        ENTER, SEND
    }
}
