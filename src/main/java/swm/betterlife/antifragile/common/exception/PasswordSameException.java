package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class PasswordSameException extends BaseException {
    public PasswordSameException() {
        super(ErrorCode.PASSWORD_SAME.getMessage());
    }
}
