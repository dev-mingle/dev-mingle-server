package com.example.dm.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserProfileData {
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("userProfile")
    private SignupUserProfilesData userProfile;
}
