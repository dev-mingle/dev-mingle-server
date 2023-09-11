package com.example.dm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private String code;
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ApiResultStatus apiResultStatus) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
        this.httpStatus = apiResultStatus.getHttpStatus();
        this.description = apiResultStatus.toString();
    }

    public BusinessException(ApiResultStatus apiResultStatus, String message) {
        this(apiResultStatus);
        this.message = String.format(apiResultStatus.getMessage(), message);
    }

}
