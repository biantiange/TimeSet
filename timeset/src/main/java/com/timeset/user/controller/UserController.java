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

    @RequestMapping("/add")
    public String insertUser(@RequestParam("phone") String phone, @RequestParam(value = "password",required = false) String password, @RequestParam(value = "userName",required = false) String userName,  @RequestParam(value = "headImg",required = false)String headImg) {
        System.out.println("插入用户");
        User user = new User();
        if(userService.findUserByPhone(phone)==null) {
            user.setPhone(phone);
            if (password != null && !password.equals("")) {
                user.setPassword(password);
            }
            if (userName != null && !userName.equals("")) {
                user.setUserName(userName);
            }
            if (headImg != null && !headImg.equals("")) {
                user.setHeadImg(headImg);
            }
            int result = userService.insertUser(user);
            if (result != 0) {
                return user.getId()+"";
            }else{
                return "注册失败！";
            }
        }else{
           return "手机号已经被注册！";
        }
    }

    @RequestMapping("/delete")
    public int deleteUser(@RequestParam("phone") String phone) {
        System.out.println("删除用户");
        int result = userService.deleteUser(phone);
        if (result != 0) {
            return 0;
        }
        return -1;
    }

    @RequestMapping("/updateUserName")
    public int updateUserNameByPhone(@RequestParam("phone") String phone, @RequestParam("userName") String userName) {
        System.out.println("修改用户名");
        int result = userService.updateUserNameByPhone(phone, userName);
        if (result != 0) {
            return 0;
        }
        return -1;
    }

    @RequestMapping("/updateUserImg")
    public int updateUserImgByPhone(@RequestParam("phone") String phone, @RequestParam("headImg") String headImg) {
        System.out.println("修改用户头像");
        int result = userService.updateUserImgByPhone(phone, headImg);
        if (result != 0) {
            return 0;
        }
        return -1;
    }

    @RequestMapping("/updateUserPassword")
    public int updateUserPasswordByPhone(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        System.out.println("修改用户密码");
        int result = userService.updateUserPasswordByPhone(phone, password);
        if (result != 0) {
            return 0;
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
