package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.config.RedisCache;
import cn.butterfly.qrscan.service.ICodeService;
import cn.butterfly.qrscan.util.CodeUtils;
import cn.butterfly.qrscan.vo.CodeVO;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import static cn.butterfly.qrscan.constant.BaseConstants.*;

/**
 * 二维码操作控制器
 *
 * @author zjw
 * @date 2021-09-12
 */
@RestController
@RequestMapping(QR_CODE_PREFIX)
public class CodeController {

    private final RedisCache redisCache;

    private final ICodeService codeService;

    public CodeController(RedisCache redisCache, ICodeService codeService) {
        this.redisCache = redisCache;
        this.codeService = codeService;
    }

    /**
     * 生成二维码内容
     *
     * @return 结果
     */
    @GetMapping("/generate")
    public BaseResult generate() {
        String code = IdUtil.simpleUUID();
        redisCache.setCacheObject(code, CodeUtils.getUnusedCodeInfo(), DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return BaseResult.success(GENERATE_SUCCESS, code);
    }

    /**
     * 扫码处理
     *
     * @param code 二维码
     * @param token token
     * @return 结果
     */
    @GetMapping("/scan")
    public BaseResult scan(String code, String token, HttpServletResponse response) {
        // 非 app 内扫码, 跳转到自定义宣传页
        if (token == null) {
            return codeService.redirectToHomePage(response);
        }
        return codeService.handleQrCode(code, token);
    }

    /**
     * 获取二维码状态信息
     *
     * @param code 二维码
     * @return 结果
     */
    @GetMapping("/info")
    public BaseResult info(String code) {
        CodeVO codeVO = redisCache.getCacheObject(code);
        if (codeVO == null) {
            return BaseResult.success(INVALID_CODE, StringUtils.EMPTY);
        }
        return BaseResult.success(GET_SUCCESS, codeVO);
    }

}
