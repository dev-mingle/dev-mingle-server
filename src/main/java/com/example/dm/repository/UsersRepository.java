package com.example.dm.repository;

import com.example.dm.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Users findByEmail(String email);
  Users findByNickname(String nickname);
}
