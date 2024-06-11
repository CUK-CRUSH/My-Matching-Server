package Dino.Duett.domain.music.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class MusicException extends CustomException {
    protected MusicException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static class MusicNotFoundException extends MusicException {
        public MusicNotFoundException() {
            super(ErrorCode.MUSIC_NOT_FOUND);
        }
    }
}
