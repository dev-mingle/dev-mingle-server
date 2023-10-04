package com.example.dm.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseStatus
public @interface ApiResponseBody {

    @AliasFor(annotation = ResponseStatus.class)
    HttpStatus value() default HttpStatus.OK;

    @AliasFor(annotation = ResponseStatus.class)
    HttpStatus code() default HttpStatus.OK;
}
