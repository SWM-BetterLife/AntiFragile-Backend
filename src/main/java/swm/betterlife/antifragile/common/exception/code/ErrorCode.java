package swm.betterlife.antifragile.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Auth
    TOKEN_EXPIRED("토큰이 만료되었습니다"),
    REFRESH_TOKEN_NOT_VALIDATED("리프레시 토큰이 유효하지 않습니다"),
    LOGIN_REQUIRED("로그인 재시도가 필요합니다"),

    // Member
    MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다"),
    HUMAN_MEMBER_CANNOT_BE_ACCESSED("휴먼 계정의 아이디의 정보는 조회할 수 없습니다"),

    // DiaryAnalysis
    DIARY_ANALYSIS_NOT_FOUND("해당 날짜에 사용자의 일기 분석을 찾을 수 없습니다"),
    DIARY_ANALYSIS_ALREADY_EXISTED("이미 해당 날짜에 사용자의 일기 분석이 존재합니다"),

    // Content
    CONTENT_NOT_FOUND("콘텐츠를 찾을 수 없습니다"),
    RECOMMENDED_CONTENT_NOT_FOUND("추천된 콘텐츠를 찾을 수 없습니다."),
    EXCESS_RECOMMEND_LIMIT("오늘의 재추천 횟수가 초과되었습니다"),
    CONTENT_ALREADY_LIKED("이미 좋아요를 누른 콘텐츠입니다"),
    CONTENT_NOT_LIKED("좋아요를 누르지 않은 콘텐츠입니다"),

    // ContentInfo
    CONTENT_INFO_NOT_FOUND("콘텐츠 정보를 찾을 수 없습니다"),

    // EmoticonTheme
    EMOTICON_THEME_NOT_FOUND("이모티콘 테마를 찾을 수 없습니다"),
    EMOTICON_THEME_ALREADY_PURCHASED("해당 이모티콘을 이미 구매하였습니다"),

    // Common
    ILLEGAL_OBJECT_ID("ObjectId가 올바르지 않습니다. 24자이어야 합니다"),
    DATABASE_UPDATE_FAIL("데이터베이스 업데이트 작업을 실패하였습니다"),
    DATABASE_UPSERT_FAIL("데이터베이스 업서트 작업을 실패하였습니다"),

    // S3
    S3_UPLOAD_FAIL("S3 업로드에 실패하였습니다"),
    S3_DELETE_FAIL("S3 삭제에 실패하였습니다"),
    S3_GENERATE_MODEL_URL_FAIL("S3 모델 URL 생성에 실패하였습니다"),

    // YouTube
    YOUTUBE_API_FAIL("유튜브 API 요청에 실패하였습니다"),

    ;

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
