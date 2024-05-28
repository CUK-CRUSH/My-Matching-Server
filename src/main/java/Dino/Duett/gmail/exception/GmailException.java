package Dino.Duett.gmail.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class GmailException extends CustomException {
    protected GmailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidContentTypeException extends GmailException {
        public InvalidContentTypeException() {
            super(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }

    public static class EmailValidationFailedException extends GmailException {
        public EmailValidationFailedException() {
            super(ErrorCode.EMAIL_VALIDATION_FAILED);
        }
    }

    public static class NoMessagesFoundException extends GmailException {
        public NoMessagesFoundException() {
            super(ErrorCode.NO_MESSAGES_FOUND);
        }
    }
}
