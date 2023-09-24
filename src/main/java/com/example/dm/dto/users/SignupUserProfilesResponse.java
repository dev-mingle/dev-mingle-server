package com.example.dm.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignupUserProfilesResponse {
    private String nickname;
    private String city;
    private String state;
    private String street;
    private String introduce;
    private String url;
    private String urlName;
}
