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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {

    private final UserProfileRepository userProfileRepository;
    private final FollowRepository followRepository;

    /**
     * 팔로우 등록
     */
    @Transactional
    public FollowInfoDto addFollow(FollowAddDto inputDto, UserProfiles currentUserProfiles) {

        // 1. 자기 자신을 팔로우하는지 확인
        if (currentUserProfiles.getId().equals(inputDto.getTargetUserProfileId())) {
            throw new FollowException(ApiResultStatus.CANNOT_FOLLOW_MYSELP); // 자기 자신을 팔로우하는 경우 에러
        }

        // 2. targetUserProfilesId 가 존재하는지 확인
        UserProfiles targetUserProfiles = userProfileRepository.findByIdAndIsDeletedIsFalse(inputDto.getTargetUserProfileId())
                .orElseThrow(() -> new FollowException(ApiResultStatus.USER_NOT_FOUND)); // 존재하지 않는 유저인 경우 에러

        // 3. 이미 팔로우한 유저인지 확인
        if (followRepository.findByUserProfiles_IdAndTargetUserProfiles_IdAndIsDeletedIsFalse(currentUserProfiles.getId(), targetUserProfiles.getId()).isPresent()) {
            throw new FollowException(ApiResultStatus.FOLLOW_ALREADY_EXIST); // 이미 팔로우한 유저인 경우 에러
        }

        // 4. 팔로우 등록
        Follows follows = Follows.create(currentUserProfiles, targetUserProfiles);
        followRepository.save(follows);

        // 5. 팔로우 등록 후 팔로우 정보 리턴
        return FollowInfoDto.convertFollows(follows);
    }

    /**
     * 팔로우 리스트 조회
     */
    @Transactional(readOnly = true)
    public Page<FollowInfoDto> showFollowList(UserProfiles currentUserProfiles, Pageable pageable) {
        try {
            // 1. 현재 유저 팔로우 리스트 조회
            Page<Follows> follows = followRepository.findByUserProfiles_IdAndIsDeletedIsFalse(currentUserProfiles.getId(), pageable);

            // 2. 팔로우 리스트 객체 return
            return follows.map(FollowInfoDto::convertFollows);

        } catch (PropertyReferenceException e) {
            throw new FollowException(ApiResultStatus.INVALID_ORDER_TYPE); // 유효하지 않은 정렬 타입인 경우 에러
        }
    }

    /**
     * 팔로우 취소
     */
    @Transactional
    public FollowInfoDto cancelFollow(Long followId, UserProfiles currentUserProfiles) {

        // 1. followId 가 자신이 팔로우한 정보인지 확인
        Follows follows = followRepository.findByIdAndUserProfiles_IdAndIsDeletedIsFalse(followId, currentUserProfiles.getId())
                .orElseThrow(() -> new FollowException(ApiResultStatus.FOLLOW_NOT_FOUND)); // 존재하지 않는 팔로우 정보인 경우 or 자신의 팔로우 정보가 아닌 경우 에러

        // 2. 팔로우 취소
        follows.delete();
        followRepository.save(follows);

        // 3. 팔로우 취소 후 팔로우 정보 리턴
        return FollowInfoDto.convertFollows(follows);
    }

}
