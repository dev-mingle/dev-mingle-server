package com.example.dm.dto.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChangePwdForm {
  @NotBlank
  private String resetPassword;

  @NotBlank
  private String password;

}
