package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class HumanMemberCannotBeAccessedException extends BaseException {
    public HumanMemberCannotBeAccessedException() {
        super(ErrorCode.HUMAN_MEMBER_CANNOT_BE_ACCESSED.getMessage());
    }
}
