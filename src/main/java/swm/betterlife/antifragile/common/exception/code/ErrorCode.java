package swm.betterlife.antifragile.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TOKEN_EXPIRED("토큰이 만료되었습니다"),
    REFRESH_TOKEN_NOT_VALIDATED("리프레시 토큰이 유효하지 않습니다"),
    LOGIN_REQUIRED("로그인 재시도가 필요합니다"),

    MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다"),

    // DiaryAnalysis
    DIARY_ANALYSIS_NOT_FOUND("해당 날짜에 사용자의 일기 분석을 찾을 수 없습니다"),
    DIARY_ANALYSIS_ALREADY_EXISTED("이미 해당 날짜에 사용자의 일기 분석이 존재합니다"),

    // Content
    CONTENT_NOT_FOUND("컨텐츠를 찾을 수 없습니다"),
    EXCESS_RECOMMEND_LIMIT("오늘의 추천 횟수가 초과되었습니다"),

    EMOTICON_THEME_NOT_FOUND("이모티콘 테마를 찾을 수 없습니다"),
    EMOTICON_THEME_ALREADY_PURCHASED("해당 이모티콘을 이미 구매하였습니다"),

    ILLEGAL_OBJECT_ID("ObjectId가 올바르지 않습니다. 24자이어야 합니다"),
    ;

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
