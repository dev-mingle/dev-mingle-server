package com.example.dm.resolver;

import com.example.dm.annotation.ApiResponseBody;
import com.example.dm.dto.ApiResponse;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.CommonException;
import com.example.dm.util.TxidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.Arrays;

@RequiredArgsConstructor
@Setter
public class ApiResponseResolver implements HandlerMethodReturnValueHandler {

    private final TxidGenerator txidGenerator;
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ApiResponseBody.class) ||
                returnType.hasMethodAnnotation(ApiResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpStatus httpStatus = getHttpStatus(returnType);
        String message = getMessage(httpStatus);

        ApiResponse apiResponse = ApiResponse.SetSuccessStatus.builder()
                .txid(txidGenerator.getTxid())
                .status(httpStatus)
                .message(message)
                .data(returnValue)
                .build();

        if (requestResponseBodyMethodProcessor == null) {
            throw new CommonException("Not found RequestResponseBodyMethodProcessor object");
        }
        requestResponseBodyMethodProcessor.handleReturnValue(apiResponse, returnType, mavContainer, webRequest);
    }

    private HttpStatus getHttpStatus(MethodParameter returnType) {
        ApiResponseBody annotation = null;
        HttpStatus httpStatus = null;


        if (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ApiResponseBody.class)) {
            annotation = AnnotatedElementUtils.findMergedAnnotation(returnType.getContainingClass(), ApiResponseBody.class);
        }

        if (returnType.hasMethodAnnotation(ApiResponseBody.class)) {
            annotation = returnType.getMethodAnnotation(ApiResponseBody.class);
        }

        if (annotation != null) {
            httpStatus = annotation.code();
        }

        if (returnType.hasMethodAnnotation(ResponseStatus.class)) {
            ResponseStatus responseStatus = returnType.getMethodAnnotation(ResponseStatus.class);
            if (responseStatus != null) {
                httpStatus = responseStatus.code();
            }
        }

        return httpStatus;
    }

    private String getMessage(HttpStatus httpStatus) {
        return Arrays.stream(ApiResultStatus.values())
                .filter(apiResultStatus -> httpStatus.equals(apiResultStatus.getHttpStatus()))
                .findAny()
                .map(ApiResultStatus::getMessage)
                .orElse(null);
    }
}
