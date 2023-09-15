package com.example.dm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResultStatus {

    REQUEST_SUCCESS("S0000", "정상적으로 처리되었습니다.", HttpStatus.OK),

    CLIENT_ERROR("E4000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("E4010", "로그인 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("E4030", "해당 권한은 호출이 불가합니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("E4040", "해당 데이터 또는 경로를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_FORMAT("E4220", "잘못된 형식의 값입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_EXTENSIONS("E4001", "올바르지 않은 확장자입니다. [%s]", HttpStatus.BAD_REQUEST),

    // 토큰 관련
    TOKEN_NOT_FOUND("E4011", "토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("E4012", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_DATE_EXPIRED("E4013", "토큰이 만료되었습니다. 보안을 위해 다시 로그인 해주세요.", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATED_FAILED("E9200", "토큰 생성 중 에러가 발생하였습니다. 다시 시도해 주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 서버 에러
    INTERNAL_SERVER_ERROR("E9000", "서버에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ACCESS_ERROR("E9100", "Database Access 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 유저 관련 에러
    ALREADY_SIGNED_UP("E4090", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    LOGIN_FAILED("E4014", "로그인 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("E4041", "유저 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("E4041", "일치하지 않는 비밀번호입니다.", HttpStatus.FORBIDDEN),

    // 게시글 관련 에러
    POST_NOT_FOUND("E4042", "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    NOT_MY_POST("E4031", "해당 게시물에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 팔로우 관련 에러
    FOLLOW_ALREADY_EXIST("E4092", "이미 팔로우한 유저입니다.", HttpStatus.CONFLICT),
    CANNOT_FOLLOW_MYSELP("E4221", "자기 자신을 지정할 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_ORDER_TYPE("E4002", "잘못된 정렬 조건 입니다.", HttpStatus.BAD_REQUEST),
    FOLLOW_NOT_FOUND("E4043", "존재하지 않는 팔로우 정보입니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}