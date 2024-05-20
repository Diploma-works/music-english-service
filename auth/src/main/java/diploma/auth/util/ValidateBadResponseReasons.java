package diploma.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateBadResponseReasons {

    public static final String BAD_AUTHORIZATION_HEADER = "Authorization header is empty incorrect";
    public static final String JWT_TOKEN_EXPIRED = "Token is expired";
}
