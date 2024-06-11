package Dino.Duett.domain.search.exception;

import Dino.Duett.global.exception.CustomException;
import Dino.Duett.global.exception.ErrorCode;

public class YoutubeException extends CustomException {
    protected YoutubeException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static class YoutubeApiRequestFailed extends YoutubeException {
        public YoutubeApiRequestFailed() {
            super(ErrorCode.YOUTUBE_API_REQUEST_FAILED);
        }
    }
    public static class YoutubeApiRequestLimitExceeded extends YoutubeException {
        public YoutubeApiRequestLimitExceeded() {
            super(ErrorCode.YOUTUBE_API_REQUEST_LIMIT_EXCEEDED);
        }
    }
}
