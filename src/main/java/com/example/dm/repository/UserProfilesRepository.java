package com.example.dm.repository;

import com.example.dm.entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long> {
  @Query(value = "SELECT COUNT(*) FROM USER_PROFILES WHERE NICKNAME=:nickname", nativeQuery = true)
  int hasNickname(String nickname);
}
