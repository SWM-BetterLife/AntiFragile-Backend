package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class EmoticonThemeNotFoundException extends BaseException {
    public EmoticonThemeNotFoundException() {
        super(ErrorCode.EMOTICON_THEME_NOT_FOUND.getMessage());
    }
}
