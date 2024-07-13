package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class ExcessRecommendLimitException extends BaseException {
    public ExcessRecommendLimitException() {
        super(ErrorCode.EXCESS_RECOMMEND_LIMIT.getMessage());
    }
}
