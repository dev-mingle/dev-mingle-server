package com.example.dm.dto.chats;

import com.example.dm.entity.UserProfiles;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileGetDto {

    private Long id;

    private String nickname;

    private String profileImageUrl;

    public static UserProfileGetDto from(UserProfiles userProfiles) {
        return new UserProfileGetDto(userProfiles.getId(), userProfiles.getNickname(), userProfiles.getImages() != null ? userProfiles.getImages().getUrl() : null);
    }
}
