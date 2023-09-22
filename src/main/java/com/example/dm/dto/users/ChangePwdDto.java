package com.example.dm.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChangePwdDto {
  @NotBlank
  private String resetPassword;

  @NotBlank
  private String password;

}
