package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class ContentAlreadyLikedException extends BaseException {
    public ContentAlreadyLikedException() {
        super(ErrorCode.CONTENT_ALREADY_LIKED.getMessage());
    }
}
