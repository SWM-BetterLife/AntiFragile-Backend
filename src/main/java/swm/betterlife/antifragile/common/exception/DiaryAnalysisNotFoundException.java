package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class DiaryAnalysisNotFoundException extends BaseException {
    public DiaryAnalysisNotFoundException() {
        super(ErrorCode.DIARY_ANALYSIS_NOT_FOUND.getMessage());
    }
}
