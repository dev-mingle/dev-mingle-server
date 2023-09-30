package com.example.dm.exception;

public class BadApiRequestException extends BusinessException{

    public BadApiRequestException(String message) {
        super(ApiResultStatus.CLIENT_ERROR, message);
    }

    public BadApiRequestException(String message, Throwable cause) {
        super(ApiResultStatus.CLIENT_ERROR, message, cause);
    }
}
