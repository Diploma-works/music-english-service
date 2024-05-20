package diploma.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class JwtConstants {

    public final static String USERNAME_TOKEN_CLAIM = "username";
    public final static String AUTHORITIES_TOKEN_CLAIM = "authorities";
    public final static String USER_ID_TOKEN_CLAIM = "user_id";
    public final static String BEARER = "Bearer";
}
