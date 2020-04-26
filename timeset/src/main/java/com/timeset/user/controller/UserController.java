package com.timeset.user.controller;

import com.timeset.user.entity.User;
import com.timeset.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/insert")
    public int insertUser(@RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("userName") String userName,  @RequestParam(value = "headImg",required = false)String headImg) {
        System.out.println("插入用户");
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setUser_name(userName);
        if (headImg != null && !headImg.equals("")) {
            user.setHead_img(headImg);
        }
        int result = userService.insertUser(user);
        if (result != 0) {
            return user.getId();
        }
        return -1;
    }

    @RequestMapping("/delete")
    public int deleteUser(@RequestParam("phone") String phone) {
        System.out.println("删除用户");
        int result = userService.deleteUser(phone);
        if (result != 0) {
            return 1;
        }
        return -1;
    }

    @RequestMapping("/updateUserNameByPhone")
    public int updateUserNameByPhone(@RequestParam("phone") String phone, @RequestParam("userName") String userName) {
        System.out.println("修改用户名");
        int result = userService.updateUserNameByPhone(phone, userName);
        if (result != 0) {
            return 1;
        }
        return -1;
    }

    @RequestMapping("/updateUserImgByPhone")
    public int updateUserImgByPhone(@RequestParam("phone") String phone, @RequestParam("headImg") String headImg) {
        System.out.println("修改用户头像");
        int result = userService.updateUserImgByPhone(phone, headImg);
        if (result != 0) {
            return 1;
        }
        return -1;
    }

    @RequestMapping("/updateUserPasswordByPhone")
    public int updateUserPasswordByPhone(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        System.out.println("修改用户密码");
        int result = userService.updateUserPasswordByPhone(phone, password);
        if (result != 0) {
            return 1;
        }
        return -1;
    }


    @RequestMapping("/all")
    public List<User> findAll() {
        System.out.println("查询所有用户");
        return userService.findAllPhone();
    }

    @RequestMapping("/findByPhone")
    public User findUserByPhone(@RequestParam("phone") String phone) {
        System.out.println("查询用户");
        return userService.findUserByPhone(phone);
    }

    ;


}
