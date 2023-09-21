package com.example.dm.repository;

import com.example.dm.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmailAndIsDeletedIsFalse(String email);
  Integer countByEmailAndIsDeletedIsFalse(String email);
}
