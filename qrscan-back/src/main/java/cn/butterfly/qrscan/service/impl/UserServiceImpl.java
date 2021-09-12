package cn.butterfly.qrscan.service.impl;

import cn.butterfly.qrscan.entity.User;
import cn.butterfly.qrscan.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author zjw
 * @date 2021-09-12
 */
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public User getByUsername(String username) {
        return new User(username, "123456");
    }

}
