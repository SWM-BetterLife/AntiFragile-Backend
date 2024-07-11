package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class IllegalObjectIdException extends BaseException {
    public IllegalObjectIdException() {
        super(ErrorCode.ILLEGAL_OBJECT_ID.getMessage());
    }
}
