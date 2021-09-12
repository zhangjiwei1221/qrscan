package cn.butterfly.qrscan.controller;

import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.entity.User;
import cn.butterfly.qrscan.service.IUserService;
import cn.butterfly.qrscan.util.JwtUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static cn.butterfly.qrscan.constant.BaseConstants.LOGIN_FAILED;

/**
 * 校验控制器
 *
 * @author zjw
 * @date 2021-09-12
 */
@RestController
public class LoginController {

    private final IUserService userService;

    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录(校验成功返回 token, 失败返回报错信息)
     *
	 * @param user 用户
     * @return 结果
     */
    @PostMapping("/login")
    public BaseResult login(@RequestBody @Validated User user) {
        User tmpUser = userService.getByUsername(user.getUsername());
        if (tmpUser != null && Objects.equals(user.getPassword(), tmpUser.getPassword())) {
            return BaseResult.success(JwtUtils.sign(user));
        }
        return BaseResult.error(LOGIN_FAILED);
    }

}
