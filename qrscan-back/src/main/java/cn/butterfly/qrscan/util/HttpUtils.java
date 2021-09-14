package cn.butterfly.qrscan.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static cn.butterfly.qrscan.constant.BaseConstants.*;

/**
 * http 工具类
 *
 * @author zjw
 * @date 2021-09-13
 */
public class HttpUtils {

    private HttpUtils() {}

    /**
     * 获取request
     *
     * @return request 对象
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    /**
     * 获取客户端操作系统名称
     *
     * @return 操作系统名称
     */
    public static String getOsName() {
        return getUserAgent().getOperatingSystem().getName();
    }

    /**
     * 获取客户端浏览器名称
     *
     * @return 浏览器名称
     */
    public static String getBrowserName() {
        return getUserAgent().getBrowser().getName();
    }

    /**
     * 获取 ip
     *
     * @return ip 地址字符串
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return UNKNOWN_IP;
        }
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (NetUtil.isUnknown(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (NetUtil.isUnknown(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (NetUtil.isUnknown(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }
        if (NetUtil.isUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        return IPV6_LOCAL_IP.equals(ip) ? IPV4_LOCAL_IP : ip;
    }

    /**
     * 根据 ip 获取地址
     *
     * @return 地址
     */
    public static String getRealAddressByIp() {
        String ip = getIp();
        // 内网不查询
        if (NetUtil.isInnerIP(ip) || UNKNOWN_IP.equals(ip)) {
            return INTERNAL_IP_MSG;
        }
        try {
            String result = HttpUtil.get(String.format(QUERY_IP_URL_FORMAT, ip), DEFAULT_TIMEOUT);
            if (StringUtils.isEmpty(result)) {
                return UNKNOWN_ADDRESS;
            }
            Address address = JsonUtils.parse(result, Address.class);
            return String.join(StringUtils.SPACE, address.getPro(), address.getCity());
        } catch (Exception e) {
            return UNKNOWN_ADDRESS;
        }
    }

    /**
     * 获取用户代理信息
     *
     * @return 代理对象
     */
    private static UserAgent getUserAgent() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return new UserAgent(StringUtils.EMPTY);
        }
        return UserAgent.parseUserAgentString(request.getHeader(USER_AGENT_HEADER));
    }

    /***
     * ip 地址查询对应实体
     *
     * @author zjw
     * @date 2021-09-14
     */
    @Data
    private static class Address {

        /**
         * 省份
         */
        private String pro;

        /***
         * 城市
         */
        private String city;

    }

}
