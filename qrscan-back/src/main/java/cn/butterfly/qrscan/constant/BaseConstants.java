package cn.butterfly.qrscan.constant;

/**
 * 基本常量类
 *
 * @author zjw
 * @date 2021-09-12
 */
public class BaseConstants {

    private BaseConstants() {}

    public static final String USERNAME = "username";

    public static final int SUCCESS = 200;

    public static final int ERROR = 500;

    public static final String UTF_8 = "UTF-8";

    public static final String SECRET = "butterfly";

    public static final String JSON_TYPE = "application/json;charset=utf-8";

    public static final String AUTHORIZATION = "Authorization";

    public static final String ILLEGAL_TOKEN = "非法 token";

    public static final String LOGIN_FAILED = "用户名或密码错误";

    public static final String USERNAME_CANT_EMPTY = "用户名不可为空";

    public static final String PASSWORD_CANT_EMPTY = "密码不可为空";

    public static final String VERIFY_API_PREFIX = "/verify";

    public static final String ALL_PATTERN = "/**";

    public static final String API_PATTERN = "/api/**";

    public static final String API_PREFIX = "/api";

    public static final String LOGIN_PATH = "/login";

    public static final String QR_CODE_PREFIX = "/code";

    public static final String ERROR_PATH = "/error";

    public static final String RESOURCE_PATTERN = "/static/**";

    public static final String RESOURCE_LOCATION = "classpath:/static/";

    public static final String FRONT_URL = "http://localhost";

    public static final String ALL = "*";

}
