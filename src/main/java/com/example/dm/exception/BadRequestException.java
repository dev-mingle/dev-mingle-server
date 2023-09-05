package com.example.dm.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

}
