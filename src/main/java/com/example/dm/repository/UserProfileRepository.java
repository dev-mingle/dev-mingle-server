package com.example.dm.repository;

import com.example.dm.entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfiles, Long> {

    Optional<UserProfiles> findByUsers_IdAndIsDeletedIsFalse(Long usersId);

}
