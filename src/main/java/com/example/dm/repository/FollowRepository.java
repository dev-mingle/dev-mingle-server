package com.example.dm.repository;

import com.example.dm.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follows, Long> {

}
