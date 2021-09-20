package cn.butterfly.qrscan.service.impl;

import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.base.CodeStatus;
import cn.butterfly.qrscan.config.RedisCache;
import cn.butterfly.qrscan.service.ICodeService;
import cn.butterfly.qrscan.util.CodeUtils;
import cn.butterfly.qrscan.util.HttpUtils;
import cn.butterfly.qrscan.util.JwtUtils;
import cn.butterfly.qrscan.vo.CodeVO;
import cn.butterfly.qrscan.vo.LoginInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static cn.butterfly.qrscan.constant.BaseConstants.*;

/**
 * 二维码操作服务实现类
 *
 * @author zjw
 * @date 2021-09-19
 */
@Service
public class CodeServiceImpl implements ICodeService {

    @Resource
    private RedisCache redisCache;

    @Override
    public BaseResult redirectToHomePage(HttpServletResponse response) {
        try {
            response.sendRedirect(BLOG_URL);
        } catch (Exception e) {
            return BaseResult.error(REDIRECT_FAILED);
        }
        return BaseResult.success(REDIRECT_SUCCESS, null);
    }

    @Override
    public BaseResult handleQrCode(String code, String token) {
        // 无效二维码处理
        CodeVO codeVO = redisCache.getCacheObject(code);
        if (codeVO == null) {
            return BaseResult.error(INVALID_CODE);
        }
        CodeStatus codeStatus = codeVO.getCodeStatus();
        if (CodeStatus.UNUSED.equals(codeStatus)) {
            return handleUnusedQr(code, token);
        }
        if (CodeStatus.CONFIRMING.equals(codeStatus)) {
            return handleConfirmingQr(code, token);
        }
        return BaseResult.error(INVALID_CODE);
    }

    /**
     * 处理未使用状态的二维码
     *
     * @param code 二维码
     * @param token token
     * @return 结果
     */
    private BaseResult handleUnusedQr(String code, String token) {
        boolean isLegal = JwtUtils.verify(token);
        if (!isLegal) {
            return BaseResult.error(AUTHENTICATION_FAILED);
        }
        String username = JwtUtils.getUsername(token);
        CodeVO codeVO = CodeUtils.getConfirmingCodeInfo(username, DEFAULT_AVATAR_URL);
        redisCache.setCacheObject(code, codeVO, DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
        String address = HttpUtils.getRealAddressByIp();
        String browser = HttpUtils.getBrowserName();
        String os = HttpUtils.getOsName();
        String tmpToken = JwtUtils.sign(username);
        redisCache.setCacheObject(tmpToken, username, DEFAULT_TEMP_TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);
        LoginInfoVO loginInfoVO = new LoginInfoVO(address, browser, os, tmpToken);
        return BaseResult.success(SCAN_SUCCESS, loginInfoVO);
    }

    /**
     * 处理未待确认状态的二维码
     *
     * @param code 二维码
     * @param token token
     * @return 结果
     */
    private BaseResult handleConfirmingQr(String code, String token) {
        String username = redisCache.getCacheObject(token);
        if (StringUtils.isBlank(username)) {
            return BaseResult.error(AUTHENTICATION_FAILED);
        }
        CodeVO codeVO = CodeUtils.getConfirmedCodeInfo(username, DEFAULT_AVATAR_URL);
        redisCache.setCacheObject(code, codeVO, DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
        String formalToken = JwtUtils.sign(username);
        return BaseResult.success(formalToken);
    }

}
