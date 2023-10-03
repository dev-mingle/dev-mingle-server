package com.example.dm.dto.chats;

import com.example.dm.entity.LoginUser;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatCreateDto {

    private Long userProfileId;

    private String sender;

    @NotEmpty(message = "메시지는 한글자 이상으로 구성되어야 합니다.")
    private String message;

    private List<String> imageUrls;

    public static ChatCreateDto from(String message, LoginUser user) {
        return ChatCreateDto.builder()
                .userProfileId(user.getId())
                .sender(user.getNickname())
                .message(message)
                .build();
    }

}
