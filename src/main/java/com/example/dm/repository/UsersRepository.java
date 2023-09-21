package com.example.dm.repository;

import com.example.dm.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmailAndIsDeletedIsFalse(String email);
  Integer countByEmailAndIsDeletedIsFalse(String email);
  @Query("SELECT u.isRandomPassword FROM Users u WHERE u.id = :id")
  Boolean findIsRandomPasswordById(@Param("id") Long id);

}
