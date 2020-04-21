package com.timeset.user.controller;

import com.timeset.user.entity.User;
import com.timeset.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserController
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:54
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;
    @RequestMapping("/all")
    public List<User> showAllPhone(){
        return userService.findAllPhone();
    }
}
