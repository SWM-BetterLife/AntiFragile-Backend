package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class LoginRequiredException extends BaseException {
    public LoginRequiredException() {
        super(ErrorCode.LOGIN_REQUIRED.getMessage());
    }
}
