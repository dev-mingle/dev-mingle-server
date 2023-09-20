package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/users")
public class UserController extends BaseController {
  private final UsersRepository usersRepository;
  private final UserProfileRepository userProfileRepository;

  /* 회원탈퇴 */
  @DeleteMapping
  public ResponseEntity<ApiResponse> deleteUser(Principal principal) {
    String email = principal.getName();
    Users user = usersRepository.findByEmailAndIsDeletedIsFalse(email).orElseThrow();
    user.delete();
    usersRepository.save(user);

    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(user.getId()).orElseThrow();
    userProfiles.delete();
    userProfileRepository.save(userProfiles);

    return responseBuilder(email, HttpStatus.OK);
  }
}
