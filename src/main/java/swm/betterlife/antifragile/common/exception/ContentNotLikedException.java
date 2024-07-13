package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class ContentNotLikedException extends BaseException {
    public ContentNotLikedException() {
        super(ErrorCode.CONTENT_NOT_LIKED.getMessage());
    }
}
