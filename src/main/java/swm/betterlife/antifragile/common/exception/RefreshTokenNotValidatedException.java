package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class RefreshTokenNotValidatedException extends BaseException {
    public RefreshTokenNotValidatedException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_VALIDATED.getMessage());
    }
}
