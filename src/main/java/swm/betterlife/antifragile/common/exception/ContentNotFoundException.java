package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class ContentNotFoundException extends BaseException {
    public ContentNotFoundException() {
        super(ErrorCode.CONTENT_NOT_FOUND.getMessage());
    }
}
