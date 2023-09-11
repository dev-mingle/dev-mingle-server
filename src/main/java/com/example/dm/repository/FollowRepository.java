package com.example.dm.repository;

import com.example.dm.entity.Follows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follows, Long> {

    Optional<Follows> findByUserProfiles_IdAndTargetUserProfiles_IdAndIsDeletedIsFalse(Long userProfileId, Long targetUserProfileId);

    Page<Follows> findByUserProfiles_IdAndIsDeletedIsFalseOrderByCreatedAtDesc(Long userProfileId, Pageable pageable);

    Page<Follows> findByUserProfiles_IdAndIsDeletedIsFalseOrderByTargetUserProfiles_NicknameAsc(Long userProfileId, Pageable pageable);

    Optional<Follows> findByIdAndUserProfiles_IdAndIsDeletedIsFalse(Long followId, Long userProfileId);

}
