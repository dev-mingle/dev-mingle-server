package com.example.dm.dto.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PreSignedUrlDto {

    @Getter
    @Setter
    @ToString
    public static class InputDto {
        private String directory;
        private List<String> extensions;
    }


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class OutputDto {
        private String extension;
        private String url;
    }

}
