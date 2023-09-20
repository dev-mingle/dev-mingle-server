package com.example.dm.chat.dto;

import com.example.dm.chat.entity.ChatRooms;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomGetDto {

    private Long roomId;

    private String name;

    private int userCount;

    public static ChatRoomGetDto from(ChatRooms chatRooms) {
        return ChatRoomGetDto.builder()
                .roomId(chatRooms.getId())
                .name(chatRooms.getName())
                .userCount(chatRooms.getUserCount())
                .build();
    }

}
