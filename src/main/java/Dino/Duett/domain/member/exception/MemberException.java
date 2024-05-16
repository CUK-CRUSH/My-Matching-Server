package Dino.Duett.domain.member.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class MemberException extends CustomException {
    protected MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static class DuplicatePhoneNumberException extends MemberException {
        public DuplicatePhoneNumberException() {
            super(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }
    }
    public static class DuplicateKakaoIdException extends MemberException {
        public DuplicateKakaoIdException() {
            super(ErrorCode.DUPLICATE_KAKAO_ID);
        }
    }
}
