package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class DatabaseUpdateFailException extends BaseException {
    public DatabaseUpdateFailException() {
        super(ErrorCode.DATABASE_UPDATE_FAIL.getMessage());
    }
}
