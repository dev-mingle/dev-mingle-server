package com.example.dm.dto.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class PasswordValidator implements ConstraintValidator<Password, String> {
  @Value("${valid.password.pattern}")
  private String PASSWORD_PATTERN;
  @Value("${valid.password.message.empty}")
  private String PASSWORD_NULL_MESSAGE;
  @Value("${valid.password.message.err}")
  private String PASSWORD_ERR_MESSAGE;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if(value.isBlank()){
      context.buildConstraintViolationWithTemplate(PASSWORD_NULL_MESSAGE)
          .addConstraintViolation();
      return false;
    }else if(!value.matches(PASSWORD_PATTERN)){
      context.buildConstraintViolationWithTemplate(PASSWORD_ERR_MESSAGE)
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}