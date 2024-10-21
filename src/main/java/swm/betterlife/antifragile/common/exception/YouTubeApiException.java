package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class YouTubeApiException extends BaseException {
    public YouTubeApiException() {
        super(ErrorCode.YOUTUBE_API_FAIL.getMessage());
    }
}
