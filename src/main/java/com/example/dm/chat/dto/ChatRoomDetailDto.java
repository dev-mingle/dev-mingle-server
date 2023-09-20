package com.example.dm.chat.dto;

import com.example.dm.chat.entity.ChatRooms;
import com.example.dm.entity.UserProfiles;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDetailDto {

    private Long roomId;

    private String roomName;

    private Long adminId;

    private String adminNickname;

    private String nickname; // todo: principaldetails 대체 예정

    public static ChatRoomDetailDto from(ChatRooms chatRooms, UserProfiles userProfiles, UserProfiles curUserProfiles) {
        return ChatRoomDetailDto.builder()
                .roomId(chatRooms.getId())
                .roomName(chatRooms.getName())
                .adminId(userProfiles.getId())
                .adminNickname(userProfiles.getNickname())
                .nickname(curUserProfiles.getNickname())
                .build();
    }

    public static ChatRoomDetailDto from(ChatRooms chatRooms) {
        return ChatRoomDetailDto.builder()
                .roomId(chatRooms.getId())
                .roomName(chatRooms.getName())
                .build();
    }
}
