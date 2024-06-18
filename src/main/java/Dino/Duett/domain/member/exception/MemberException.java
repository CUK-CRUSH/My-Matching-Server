package Dino.Duett.domain.member.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class MemberException extends CustomException {
    protected MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    // PhoneNumber 중복 예외
    public static class DuplicatePhoneNumberException extends MemberException {
        public DuplicatePhoneNumberException() {
            super(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }
    }

    // KakaoId 중복 예외
    public static class DuplicateKakaoIdException extends MemberException {
        public DuplicateKakaoIdException() {
            super(ErrorCode.DUPLICATE_KAKAO_ID);
        }
    }

    // Role 찾을 수 없는 예외
    public static class RoleNotFoundException extends MemberException {
        public RoleNotFoundException() {
            super(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    // Member 찾을 수 없는 예외
    public static class MemberNotFoundException extends MemberException {
        public MemberNotFoundException() {
            super(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    // 토큰이 유효하지 않은 예외
    public static class InvalidTokenException extends MemberException {
        public InvalidTokenException() {
            super(ErrorCode.INVALID_TOKEN);
        }

        public static class MemberCoinNotEnoughException extends MemberException {
            public MemberCoinNotEnoughException() {
                super(ErrorCode.MEMBER_COIN_NOT_ENOUGH);
            }
        }
    }
}
