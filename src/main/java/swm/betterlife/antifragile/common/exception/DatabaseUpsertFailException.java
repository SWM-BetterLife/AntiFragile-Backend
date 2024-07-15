package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class DatabaseUpsertFailException extends BaseException {
    public DatabaseUpsertFailException() {
        super(ErrorCode.DATABASE_UPSERT_FAIL.getMessage());
    }
}
