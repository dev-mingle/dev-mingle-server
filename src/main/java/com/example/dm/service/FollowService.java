package com.example.dm.service;

import com.example.dm.dto.follows.FollowAddDto;
import com.example.dm.dto.follows.FollowInfoDto;
import com.example.dm.entity.Follows;
import com.example.dm.entity.UserProfiles;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.FollowException;
import com.example.dm.repository.FollowRepository;
import com.example.dm.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {

    private final UserProfileRepository userProfileRepository;
    private final FollowRepository followRepository;

    @Transactional
    public FollowInfoDto addFollows(FollowAddDto inputDto, UserProfiles currentUserProfiles) {

        // 1. 자기 자신을 팔로우하는지 확인
        if (currentUserProfiles.getId().equals(inputDto.getTargetUserProfileId())) {
            throw new FollowException(ApiResultStatus.NOT_FOLLOW_MYSELP); // 자기 자신을 팔로우하는 경우 에러
        }

        // 2. targetUserProfilesId가 존재하는지 확인
        UserProfiles targetUserProfiles = userProfileRepository.findByIdAndIsDeletedIsFalse(inputDto.getTargetUserProfileId())
                .orElseThrow(() -> new FollowException(ApiResultStatus.USER_NOT_FOUND)); // 존재하지 않는 유저인 경우 에러

        // 3. 이미 팔로우한 유저인지 확인
        if (followRepository.findByUserProfiles_IdAndTargetUserProfiles_IdAndIsDeletedIsFalse(currentUserProfiles.getId(), targetUserProfiles.getId()).isPresent()) {
            throw new FollowException(ApiResultStatus.FOLLOW_ALREADY_EXIST); // 이미 팔로우한 유저인 경우 에러
        }

        // 4. 팔로우 등록
        Follows follows = Follows.builder()
                .userProfiles(currentUserProfiles)
                .targetUserProfiles(targetUserProfiles)
                .build();

        followRepository.save(follows);

        // 5. 팔로우 등록 후 팔로우 정보 리턴
        return FollowInfoDto.convertFollows(follows);
    }

}
