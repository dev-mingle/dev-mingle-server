package com.example.dm.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatDto {

    private Long roomId;

    private String sender;

    private String message;

}
