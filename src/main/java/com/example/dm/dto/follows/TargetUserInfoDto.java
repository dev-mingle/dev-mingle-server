package com.example.dm.dto.follows;

import com.example.dm.entity.Follows;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class TargetUserInfoDto {

    private Long id; // 유저 프로필 id
    private String imageUrl; // 프로필 이미지
    private String nickname; // 닉네임

    // Follows -> TargetUserInfoDto
    public static TargetUserInfoDto convertFollows(Follows follows) {
        return TargetUserInfoDto.builder()
                .id(follows.getTargetUserProfiles().getId())
                .imageUrl(follows.getTargetUserProfiles().getImages().getUrl())
                .nickname(follows.getTargetUserProfiles().getNickname())
                .build();
    }

}
