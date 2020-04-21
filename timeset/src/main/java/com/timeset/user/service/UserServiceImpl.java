package com.timeset.user.service;

import com.timeset.user.entity.User;
import com.timeset.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 17:06
 */
@Service
public class UserServiceImpl {
    @Resource
    private UserMapper userMapper;

    public List<User> findAllPhone() {
        return userMapper.selectAllPhone();
    }
}
