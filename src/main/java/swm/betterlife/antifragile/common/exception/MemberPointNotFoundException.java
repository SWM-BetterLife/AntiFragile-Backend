package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class MemberPointNotFoundException extends BaseException {
    public MemberPointNotFoundException() {
        super(ErrorCode.MEMBER_POINT_NOT_FOUND.getMessage());
    }
}
