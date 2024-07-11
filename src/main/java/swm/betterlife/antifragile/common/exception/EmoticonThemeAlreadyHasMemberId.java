package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class EmoticonThemeAlreadyHasMemberId extends BaseException {
    public EmoticonThemeAlreadyHasMemberId() {
        super(ErrorCode.EMOTICON_THEME_ALREADY_PURCHASED.getMessage());
    }
}
