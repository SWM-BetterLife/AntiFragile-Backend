package swm.betterlife.antifragile.common.jwt.constant;

public class JwtConstant {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

//    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    public static final String LOGIN_TYPE_KEY = "loginType";
    public static final String AUTHORITIES_KEY = "authorities";
}
