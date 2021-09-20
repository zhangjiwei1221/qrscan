package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.config.RedisCache;
import cn.butterfly.qrscan.service.ICodeService;
import cn.butterfly.qrscan.util.CodeUtils;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 二维码生成信息模板字符串
     */
    @Value("${code.qrcodeTemplate}")
    private String qrcodeTemplate;

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
        return BaseResult.success(GENERATE_SUCCESS, String.format(qrcodeTemplate, code));
    }

    /**
     * 二维码处理
     *
     * @param code 二维码
     * @param token token
     * @return 结果
     */
    @GetMapping("/qrcode")
    public BaseResult qrcode(String code, String token, HttpServletResponse response) {
        // 非 app 内扫码, 跳转到自定义宣传页
        if (token == null) {
            return codeService.redirectToHomePage(response);
        }
        return codeService.handleQrCode(code, token);
    }

}
