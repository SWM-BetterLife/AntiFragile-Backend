package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class RecommendedContentNotFoundException extends BaseException {
    public RecommendedContentNotFoundException() {
        super(ErrorCode.RECOMMENDED_CONTENT_NOT_FOUND.getMessage());
    }
}
