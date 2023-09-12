package com.example.dm.dto.form;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
public class SignupUserData {
    private String email;
    private LocalDateTime created_at;
    private SignupUserProfilesData user_profile;
}
