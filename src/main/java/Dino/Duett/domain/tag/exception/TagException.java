package Dino.Duett.domain.tag.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class TagException extends CustomException {
    protected TagException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class TagNotFoundException extends TagException {
        public TagNotFoundException() {
            super(ErrorCode.TAG_NOT_FOUND);
        }
    }
    public static class TagTypeNotFoundException extends TagException {
        public TagTypeNotFoundException() {
            super(ErrorCode.TAG_TYPE_NOT_FOUND);
        }
    }
    public static class TagMaxLimitException extends TagException {
        public TagMaxLimitException() {
            super(ErrorCode.TAG_MAX_LIMIT);
        }
    }

}
