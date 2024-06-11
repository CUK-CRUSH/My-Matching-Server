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

    public static class MessageNotFoundException extends GmailException {
        public MessageNotFoundException() {
            super(ErrorCode.MESSAGE_NOT_FOUND);
        }
    }
}
