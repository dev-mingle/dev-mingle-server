package com.example.dm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends BusinessException {

    private String code;
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public CommonException() {
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(ApiResultStatus apiResultStatus) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
        this.httpStatus = apiResultStatus.getHttpStatus();
        this.description = apiResultStatus.toString();
    }

    public CommonException(ApiResultStatus apiResultStatus, String message) {
        this(apiResultStatus);
        this.message = String.format(apiResultStatus.getMessage(), message);
    }

}
