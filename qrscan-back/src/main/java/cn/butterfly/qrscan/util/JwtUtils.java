package cn.butterfly.qrscan.util;

import cn.butterfly.qrscan.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static cn.butterfly.qrscan.constant.BaseConstants.SECRET;
import static cn.butterfly.qrscan.constant.BaseConstants.USERNAME;

/**
 * jwt 工具类
 *
 * @author zjw
 * @date 2021-09-12
 */
public class JwtUtils {

    private JwtUtils() {}

    /**
     * 过期时间 7 天(毫秒)
     */
    private static final long EXPIRE_TIME = (long) 7 * 24 * 60 * 60 * 1000;


    /**
     * 生成 token
     *
     * @param user 用户
     * @return token
     */
    public static String sign(User user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return createToken(user.getUsername(), date);
    }

    /**
     * 生成 token(包含用户名信息)
     *
     * @param username 用户名
     * @param date 有效期
     * @return token
     */
    private static String createToken(String username, Date date) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withClaim(USERNAME, username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验 token 是否合法
     *
     * @param token 密钥
     * @return 是否合法
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得 token 中保存的用户名信息
     *
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USERNAME).asString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

}
