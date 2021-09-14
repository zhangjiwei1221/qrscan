package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.config.RedisCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static cn.butterfly.qrscan.constant.BaseConstants.QR_CODE_PREFIX;

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

    public CodeController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

}
