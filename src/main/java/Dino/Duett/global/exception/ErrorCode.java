package Dino.Duett.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {
    // Basic HTTP Status Code
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    // 1000: Authentication
    INVALID_VERIFICATION_CODE(1000, "인증 코드가 유효하지 않습니다"),
    VERIFICATION_CODE_NOT_FOUND(1001, "인증 코드를 찾을 수 없습니다"),
    LOGIN_FAILED(1002, "로그인 실패"),

    // 2000: Member
    DUPLICATE_PHONE_NUMBER(2000, "전화번호가 중복됩니다"),
    DUPLICATE_KAKAO_ID(2001, "카카오 아이디가 중복됩니다"),
    ROLE_NOT_FOUND(2002, "역할을 찾을 수 없습니다"),
    MEMBER_NOT_FOUND(2003, "사용자를 찾을 수 없습니다"),
    INVALID_TOKEN(2004, "토큰이 유효하지 않습니다"),

    // 3000: Gmail
    INVALID_CONTENT_TYPE(3000, "올바르지 않은 메일 형식"),
    EMAIL_VALIDATION_FAILED(3001, "이메일 유효성 검사 실패"),
    MESSAGE_NOT_FOUND(3002, "메일을 찾을 수 없습니다"),
    ;

    private final int code;
    private final String message;
}
