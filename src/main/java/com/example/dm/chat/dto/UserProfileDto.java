package com.example.dm.chat.dto;

import com.example.dm.entity.UserProfiles;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDto {

    private Long id;

    private String nickname;

    public static UserProfileDto from(UserProfiles userProfiles) {
        return new UserProfileDto(userProfiles.getId(), userProfiles.getNickname());
    }
}
