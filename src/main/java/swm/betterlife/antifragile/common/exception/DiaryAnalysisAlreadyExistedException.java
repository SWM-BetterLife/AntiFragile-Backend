package swm.betterlife.antifragile.common.exception;

import swm.betterlife.antifragile.common.exception.code.ErrorCode;

public class DiaryAnalysisAlreadyExistedException extends BaseException {
    public DiaryAnalysisAlreadyExistedException() {
        super(ErrorCode.DIARY_ANALYSIS_ALREADY_EXISTED.getMessage());
    }
}