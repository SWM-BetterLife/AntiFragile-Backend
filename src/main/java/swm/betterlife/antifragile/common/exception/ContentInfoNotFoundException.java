package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class ContentInfoNotFoundException extends BaseException {
    public ContentInfoNotFoundException() {
        super(ErrorCode.CONTENT_INFO_NOT_FOUND.getMessage());
    }
}
