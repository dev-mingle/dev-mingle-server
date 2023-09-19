package com.example.dm.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomGetDto {

    private Long roomId;

    private String name;

    private int userCount;

}
