package com.example.dm.dto.chats;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChatRoomCreateDto {

    @NotBlank(message = "채팅방 제목은 공백이 아닌 한글자 이상이어야 합니다.")
    private String name;
}
