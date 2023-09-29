package com.example.dm.dto.users;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class PasswordValidator implements ConstraintValidator<Password, String> {
  private static final Logger logger = LoggerFactory.getLogger(PasswordValidator.class);

  @Value("${valid.password.pattern}")
  private String PASSWORD_PATTERN;
  @Value("${valid.password.message.empty}")
  private String PASSWORD_NULL_MESSAGE;
  @Value("${valid.password.message.err}")
  private String PASSWORD_ERR_MESSAGE;

  @PostConstruct
  public void postConstruct() {
    logger.info("PASSWORD_PATTERN: {}", PASSWORD_PATTERN);
    logger.info("PASSWORD_NULL_MESSAGE: {}", PASSWORD_NULL_MESSAGE);
    logger.info("PASSWORD_ERR_MESSAGE: {}", PASSWORD_ERR_MESSAGE);
  }

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
