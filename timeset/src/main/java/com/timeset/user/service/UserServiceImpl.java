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

    public int insertUser(User user){return userMapper.insertUser(user);};

    public int deleteUser(String phone){return userMapper.deleteUser(phone);};

    public int updateUserNameByPhone(String phone,String userName){return userMapper.updateUserNameByPhone(phone,userName);};

    public  int updateUserImgByPhone(String phone,String headImg){return userMapper.updateUserImgByPhone(phone,headImg);};

    public  int updateUserPasswordByPhone(String phone,String password){return userMapper.updateUserPasswordByPhone(phone,password);};

    public List<User> findAllPhone(){return userMapper.findAllPhone();};

    public User findUserByPhone(String phone){return userMapper.findUserByPhone(phone);};
}
