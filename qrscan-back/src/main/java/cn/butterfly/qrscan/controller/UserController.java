package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.base.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.butterfly.qrscan.constant.BaseConstants.API_PREFIX;

/**
 * 用户控制器
 *
 * @author zjw
 * @date 2021-09-12
 */
@RestController
@RequestMapping(API_PREFIX)
public class UserController {

    @GetMapping("/demo")
    public BaseResult demo() {
        return BaseResult.success();
    }

}
