package com.example.dm.exception;

public class UpdateFailureException extends BusinessException{

    public UpdateFailureException(String message) {
        super(ApiResultStatus.RESPONSE_FAILED_ERROR, message);
    }

    public UpdateFailureException(String message, Throwable cause) {
        super(ApiResultStatus.RESPONSE_FAILED_ERROR, message, cause);
    }
}
