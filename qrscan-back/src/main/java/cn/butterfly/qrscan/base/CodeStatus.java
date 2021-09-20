package cn.butterfly.qrscan.base;

/**
 * 二维码状态枚举类
 *
 * @author zjw
 * @date 2021-09-17
 */
public enum CodeStatus {

    /**
     * 过期
     */
    EXPIRE,

    /**
     * 未使用的二维码
     */
    UNUSED,

    /**
     * 已扫码, 等待确认
     */
    CONFIRMING,

    /**
     * 确认登录成功
     */
    CONFIRMED

}
