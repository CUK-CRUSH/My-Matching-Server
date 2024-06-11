package Dino.Duett.domain.mood.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class MoodException extends CustomException {
    protected MoodException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static class MoodNotFoundException extends MoodException {
        public MoodNotFoundException() {
            super(ErrorCode.MOOD_NOT_FOUND);
        }
    }
    public static class MoodForbiddenException extends MoodException {
        public MoodForbiddenException() {
            super(ErrorCode.MOOD_FORBIDDEN);
        }
    }
}
