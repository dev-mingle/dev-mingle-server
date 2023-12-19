package com.example.dm.dto.commons;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

public class KafkaDto {

    @Getter
    @Setter
    @ToString
    public static class InputDto {
        private String topic;
        private String message;
    }

    @Getter
    @Setter
    @ToString
    public static class OutputDto {
        private String topic;
        private String message;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime timestamp;

        public OutputDto(InputDto inputDto) {
            this.topic = inputDto.getTopic();
            this.message = inputDto.getMessage();
            this.timestamp = LocalDateTime.now();
        }
    }

}
