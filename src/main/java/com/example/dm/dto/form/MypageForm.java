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
public class MypageForm {
    @NotBlank
    private Long id;

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
