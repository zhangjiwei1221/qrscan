package cn.butterfly.qrscan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录信息值对象
 *
 * @author zjw
 * @date 2021-09-18
 */
@Data
@AllArgsConstructor
public class LoginInfoVO {

    /**
     * 登录地址
     */
    private String address;

    /**
     * 登录的浏览器名称
     */
    private String browser;

    /**
     * 登录的操作系统名称
     */
    private String os;

    /**
     * 临时认证 token
     */
    private String token;

}
