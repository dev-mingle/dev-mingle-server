package com.example.dm.dto.follows;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FollowAddDto {

    private Long targetUserProfileId; // 팔로우 대상 유저 프로필 id

}
