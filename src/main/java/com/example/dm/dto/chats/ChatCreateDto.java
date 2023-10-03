package com.example.dm.dto.chats;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatCreateDto {

    @NotEmpty(message = "메시지는 한글자 이상으로 구성되어야 합니다.")
    private String message;

    private List<String> imageUrls;

    public static ChatCreateDto from(String message) {
        return ChatCreateDto.builder()
                .message(message)
                .build();
    }

}
