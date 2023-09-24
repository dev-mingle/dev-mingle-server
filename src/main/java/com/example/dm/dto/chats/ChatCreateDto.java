package com.example.dm.dto.chats;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatCreateDto {

    private Long roomId;

    private String sender;

    @NotEmpty(message = "메시지는 한글자 이상으로 구성되어야 합니다.")
    private String message;

    public static ChatCreateDto from(ChatRoomDetailDto chatRoomDetailDto, String message) {
        return ChatCreateDto.builder()
                .message(message)
                .roomId(chatRoomDetailDto.getRoomId())
                .sender(chatRoomDetailDto.getNickname())
                .build();
    }

}
