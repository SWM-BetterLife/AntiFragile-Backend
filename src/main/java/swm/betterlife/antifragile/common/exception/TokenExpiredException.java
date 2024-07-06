package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED.getMessage());
    }
}
