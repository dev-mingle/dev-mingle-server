package com.example.dm.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

    private Long roomId;

    private String sender;

    private String message;

    private String createdAt;

    private MessageType type;

    public enum MessageType{
        ENTER, SEND
    }
}
