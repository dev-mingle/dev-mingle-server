package com.example.dm.dto.chats;

import com.example.dm.entity.ChatRooms;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomGetDto {

    private Long roomId;

    private String name;

    private int userCount;

    private int capacity;

    public static ChatRoomGetDto from(ChatRooms chatRooms) {
        return ChatRoomGetDto.builder()
                .roomId(chatRooms.getId())
                .name(chatRooms.getName())
                .userCount(chatRooms.getUserCount())
                .capacity(chatRooms.getCapacity())
                .build();
    }

}
