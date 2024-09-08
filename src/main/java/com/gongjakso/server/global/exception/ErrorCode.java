package com.gongjakso.server.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    // 1000: Success Case
    SUCCESS(HttpStatus.OK, 1000, "정상적인 요청입니다."),
    CREATED(HttpStatus.CREATED, 1001, "정상적으로 생성되었습니다."),

    // 2000: Common Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 2000, "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 리소스입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, 2002, "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, 2003, "권한이 없는 요청입니다."),
    ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, 2004, "이미 삭제된 리소스입니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, 2005, "인가되지 않는 요청입니다."),
    ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, 2006, "이미 존재하는 리소스입니다."),


    // 3000: Auth Error
    KAKAO_TOKEN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 3000, "토큰 발급에서 오류가 발생했습니다."),
    KAKAO_USER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 3001, "카카오 프로필 정보를 가져오는 과정에서 오류가 발생했습니디."),
    WRONG_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, 3002, "유효하지 않은 토큰입니다."),
    LOGOUT_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, 3003, "로그아웃된 토큰입니다"),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, 3004, "유효하지 않은 토큰입니다."),


    // 4000: Contest Error
    CONTEST_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 4000, "존재하지 않는 공모전입니다."),

    // 5000: Team Error
    TEAM_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 5000, "존재하지 않는 팀입니다."),
    INVALID_POSITION_EXCEPTION(HttpStatus.BAD_REQUEST, 5001, "올바르지 않은 포지션입니다."),

    // 6000: Portfolio Error
    PORTFOLIO_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 6000, "존재하지 않는 포트폴리오입니다."),

    //7000: Apply Error
    APPLY_PERIOD_EXPIRED_EXCEPTION(HttpStatus.BAD_REQUEST,7000,"지원 기간이 지났습니다"),
    APPLY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,7002,"존재하지 않는 지원서입니다."),
    APPLY_ALREADY_EXISTS_EXCEPTION(HttpStatus.BAD_REQUEST, 7003, "이미 지원했습니다."),
    APPLY_LEADER_NOT_ALLOWED_EXCEPTION(HttpStatus.BAD_REQUEST, 7011, "팀장은 지원할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
