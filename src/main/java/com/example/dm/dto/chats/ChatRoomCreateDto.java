package com.example.dm.dto.chats;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChatRoomCreateDto {

    private String name;
}
