package com.timeset.user.mapper;

import com.timeset.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:53
 */
public interface UserMapper {
    //增加用户
    public int insertUser(User user);

    //根据id或者phone删除用户
    public int deleteUser(String phone);

    //根据id或者phone修改用户名
    public int updateUserNameByPhone(String phone,String userName);

    //根据id或者phone修改用户头像
    public  int updateUserImgByPhone(String phone,String headImg);

    //根据id和手机号修改密码
    public  int updateUserPasswordByPhone(String phone,String password);

    //查询所有用户
    public List<User> findAllPhone();

    //根据id或者phone查询用户
    public User findUserByPhone(String phone);

    //登录
    public User findByPhoneAndPassword(@Param("phone")String phone,@Param("password")String password);
}
