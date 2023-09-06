package com.example.dm.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SignupForm {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
             message = "비밀번호는 최소 8자 이상, 영문 대소문자, 숫자, 특수 문자 포함해야 합니다.")
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String street;

    private Double latitude;
    private Double longitude;
    private String introduce;
    private String url;
    private String urlName;
}
