package com.example.dm.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDto {

    private Long roomId;

    private String roomName;

    private Long userProfileId;

    private String nickname;
}
