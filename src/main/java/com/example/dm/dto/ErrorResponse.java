package com.example.dm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String code;
    private String description;
}
