package com.example.dm.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatCreateDto {

    private Long roomId;

    private String sender;

    private String message;

    public static ChatCreateDto from(ChatRoomDetailDto chatRoomDetailDto, String message) {
        return ChatCreateDto.builder()
                .message(message)
                .roomId(chatRoomDetailDto.getRoomId())
                .sender(chatRoomDetailDto.getNickname())
                .build();
    }

}
