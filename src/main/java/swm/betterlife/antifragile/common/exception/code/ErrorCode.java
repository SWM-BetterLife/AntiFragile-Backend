package swm.betterlife.antifragile.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TOKEN_EXPIRED("토큰이 만료되었습니다"),
    REFRESH_TOKEN_NOT_VALIDATED("리프레시 토큰이 유효하지 않습니다"),
    LOGIN_REQUIRED("로그인 재시도가 필요합니다"),
    ;

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
