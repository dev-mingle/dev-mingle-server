package com.example.dm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FollowException extends BusinessException {

    private String code;
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public FollowException() {
    }

    public FollowException(String message) {
        super(message);
    }

    public FollowException(ApiResultStatus apiResultStatus) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
        this.httpStatus = apiResultStatus.getHttpStatus();
        this.description = apiResultStatus.toString();
    }

    public FollowException(ApiResultStatus apiResultStatus, String message) {
        this(apiResultStatus);
        this.message = message;
    }

}
