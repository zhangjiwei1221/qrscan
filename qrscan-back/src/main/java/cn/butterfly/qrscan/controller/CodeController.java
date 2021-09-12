package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.config.RedisCache;
import cn.butterfly.qrscan.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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

    @PostMapping("/set")
    public void set() {
        redisCache.setCacheObject("user", new User("butterfly", "123456"), 10, TimeUnit.SECONDS);
    }

    @GetMapping("/get")
    public BaseResult get() {
        User user = redisCache.getCacheObject("user");
        return BaseResult.success(user);
    }

}
