package cn.butterfly.qrscan.util;

import cn.butterfly.qrscan.base.CodeStatus;
import cn.butterfly.qrscan.vo.CodeVO;

/**
 * 二维码工具类
 *
 * @author zjw
 * @date 2021-09-18
 */
public class CodeUtils {

    private CodeUtils() {}

    /**
     * 获取过期二维码存储信息
     *
     * @return 二维码值对象
     */
    public static CodeVO getExpireCodeInfo() {
        return new CodeVO(CodeStatus.EXPIRE);
    }

    /**
     * 获取未使用二维码存储信息
     *
     * @return 二维码值对象
     */
    public static CodeVO getUnusedCodeInfo() {
        return new CodeVO(CodeStatus.UNUSED);
    }

    /**
     * 获取已扫码二维码存储信息
     *
     * @param username 用户名
     * @param avatar 头像
     * @return 二维码值对象
     */
    public static CodeVO getConfirmingCodeInfo(String username, String avatar) {
        return new CodeVO(CodeStatus.CONFIRMING, username, avatar);
    }

    /**
     * 获取已扫码确认二维码存储信息
     *
     * @param username 用户名
     * @param avatar 头像
     * @param token token
     * @return 二维码值对象
     */
    public static CodeVO getConfirmedCodeInfo(String username, String avatar, String token) {
        return new CodeVO(CodeStatus.CONFIRMED, username, avatar, token);
    }

}
