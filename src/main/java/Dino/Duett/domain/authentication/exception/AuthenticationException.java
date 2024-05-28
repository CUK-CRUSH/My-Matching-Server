package Dino.Duett.domain.authentication.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class AuthenticationException extends CustomException {
    protected AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidVerificationCodeException extends AuthenticationException {
        public InvalidVerificationCodeException() {
            super(ErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    public static class VerificationCodeNotFoundException extends AuthenticationException {
        public VerificationCodeNotFoundException() {
            super(ErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }
    }

}
