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
    MEMBER_COIN_NOT_ENOUGH(2005, "코인이 부족합니다"),

    // 3000: Gmail
    INVALID_CONTENT_TYPE(3000, "올바르지 않은 메일 형식"),
    EMAIL_VALIDATION_FAILED(3001, "이메일 유효성 검사 실패"),
    MESSAGE_NOT_FOUND(3002, "메일을 찾을 수 없습니다"),

    // 4000: Image
    IMAGE_NOT_FOUND(4000, "이미지를 찾을 수 없습니다"),
    IMAGE_SAVE_FAILED(4001, "이미지 저장 실패"),
    IMAGE_CONVERT_FAILED(4002, "이미지 변환 실패"),
    MULTIPART_FILE_CONVERT_FAILED(4003, "MultipartFile 변환 실패"),
    IMAGE_DELETE_FAILED(4004, "이미지 삭제 실패"),

    // 4000: Profile
    PROFILE_NOT_FOUND(5000, "프로필을 찾을 수 없습니다."),
    GENDER_TYPE_NOT_FOUND(5001, "성별 타입을 찾을 수 없습니다."),
    MBTI_TYPE_NOT_FOUND(5002, "MBTI 타입을 찾을 수 없습니다."),
    PROFILE_FORBIDDEN(5003, "프로필 접근 권한이 없습니다."),

    // 5000: Music
    MUSIC_NOT_FOUND(6000, "음악을 찾을 수 없습니다"),

    // 6000: Mood
    MOOD_NOT_FOUND(7000, "무드를 찾을 수 없습니다."),
    MOOD_FORBIDDEN(7001, "무드 접근 권한이 없습니다."),

    // 7000: Tag
    TAG_NOT_FOUND(8000, "태그를 찾을 수 없습니다."),
    TAG_TYPE_NOT_FOUND(8001, "태그 타입을 찾을 수 없습니다."),
    TAG_MAX_LIMIT(8002, "태그 최대 개수 초과"),

    // 8000: Youtube
    YOUTUBE_API_REQUEST_FAILED(9000, "Youtube API 요청 실패"),
    YOUTUBE_API_REQUEST_LIMIT_EXCEEDED(9001, "Youtube API 키 사용량 초과");

    private final int code;
    private final String message;
}
