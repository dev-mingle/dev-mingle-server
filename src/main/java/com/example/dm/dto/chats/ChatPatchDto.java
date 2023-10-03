package com.example.dm.dto.chats;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ChatPatchDto {

    private String messageId;

    @NotEmpty(message = "메시지는 한글자 이상으로 구성되어야 합니다.")
    private String message;

}
