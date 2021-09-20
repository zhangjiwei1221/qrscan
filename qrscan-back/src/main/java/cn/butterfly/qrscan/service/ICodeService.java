package cn.butterfly.qrscan.service;

import cn.butterfly.qrscan.base.BaseResult;
import javax.servlet.http.HttpServletResponse;

/**
 * 二维码操作服务类
 *
 * @author zjw
 * @date 2021-09-19
 */
public interface ICodeService {

    /**
     * 重定向到自定义宣传主页
     *
	 * @param response 响应对象
     * @return 结果
     */
    BaseResult redirectToHomePage(HttpServletResponse response);

    /**
     * 处理二维码操作
     *
	 * @param code 二维码
	 * @param token token
     * @return 结果
     */
    BaseResult handleQrCode(String code, String token);

}
