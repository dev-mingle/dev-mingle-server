package com.example.dm.repository;

import com.example.dm.entity.Posts;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostsJpaRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p left join fetch p.userProfile where p.id = :id and p.isDeleted = false")
    Optional<Posts> findByIdJoinUserProfile(Long id);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select p from Posts p where p.id = :id and p.isDeleted = false")
    Optional<Posts> findByIdWithOptimisticLock(Long id);
}
