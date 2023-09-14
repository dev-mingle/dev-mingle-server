package com.example.dm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends BusinessException {

  private String code;
  private String message;
  private String description;
  private HttpStatus httpStatus;

  public AuthException() {
  }

  public AuthException(String message) {
    super(message);
  }

  public AuthException(ApiResultStatus apiResultStatus) {
    this.code = apiResultStatus.getCode();
    this.message = apiResultStatus.getMessage();
    this.httpStatus = apiResultStatus.getHttpStatus();
    this.description = apiResultStatus.toString();
  }

  public AuthException(ApiResultStatus apiResultStatus, String message) {
    this(apiResultStatus);
    this.message = String.format(apiResultStatus.getMessage(), message);
  }

}