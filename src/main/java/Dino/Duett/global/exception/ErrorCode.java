package Dino.Duett.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
    INVALID_VERIFICATION_CODE(1000, "verification code is invalid"),

    // 2000: Member
    DUPLICATE_PHONE_NUMBER(2000, "phone number is already in use"),
    DUPLICATE_KAKAO_ID(2001, "kakao id is already in use"),
    ;

    private final int code;
    private final String message;
}
