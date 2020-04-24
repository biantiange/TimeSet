package com.timeset.user.mapper;

import com.timeset.user.entity.User;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:53
 */
public interface UserMapper {
    public List<User> selectAllPhone();
    public User findByPhone(String phone);
}
