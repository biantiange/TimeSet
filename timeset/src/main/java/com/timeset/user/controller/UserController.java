package com.timeset.user.controller;

import com.timeset.photo.entity.Photo;
import com.timeset.user.entity.User;
import com.timeset.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

    @RequestMapping("/registry")
    public String insertUser(@RequestParam("phone") String phone,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "userName",required = false) String userName,
                             @RequestParam(value = "file",required = false) MultipartFile file,
                             HttpServletRequest request) {
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
            if (file != null) {
                //上传服务器
                // 生成新的文件名
                String fileName = System.currentTimeMillis()+file.getOriginalFilename();
                // 保存路径
//                String destFileName=request.getServletContext().getRealPath("")+"headImg"+ File.separator+fileName;
                String destFileName=request.getServletContext().getRealPath("")+"headImg/"+fileName;
                // 执行保存操作
                File destFile = new File(destFileName);
                if (!destFile.getParentFile().exists()){
                    destFile.getParentFile().mkdir();
                }
                try {
                    file.transferTo(destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.setHeadImg("headImg/"+fileName);
            }
            int result = userService.insertUser(user);
            if (result != 0) {
                return "OK";
            }else{
                return "NO";
            }
        }else{
           return "";
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
    public int updateUserImgByPhone(@RequestParam("phone") String phone
            ,  @RequestParam(value = "file",required = false) MultipartFile file
            ,HttpServletRequest request) {
        System.out.println("修改用户头像");
        if (file != null) {
            //上传服务器
            // 生成新的文件名
            String headImg = System.currentTimeMillis()+file.getOriginalFilename();
            // 保存路径
            String destFileName=request.getServletContext().getRealPath("")+"headImg"+ File.separator+headImg;
            System.out.println(destFileName);
            // 执行保存操作
            File destFile = new File(destFileName);
            if (!destFile.getParentFile().exists()){
                destFile.getParentFile().mkdir();
            }
            try {
                file.transferTo(destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int result = userService.updateUserImgByPhone(phone,headImg);
            if (result != 0) {
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("updateUserPassword")
    public int updatePasswordByPhone(@RequestParam("phone") String phone,@RequestParam("password") String password){
        System.out.println("个人——修改用户密码");
        int result = userService.updateUserPasswordByPhone(phone, password);
        if (result != 0) {
            return 0;
        }
        return -1;
    }

    @RequestMapping("updateUserPhone")
    public int updateUserPhoneByUserId(@RequestParam("phone") String phone,@RequestParam("id") String id){
        System.out.println("个人——修改用户手机号");
        if (userService.findUserByPhone(phone) != null){
            return -2;
        }
        int result = userService.updateUserPhoneByUserId(phone, id);
        if (result != 0) {
            return 0;
        }
        return -1;
    }

    @RequestMapping("/forget")
    public String updateUserPasswordByPhone(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        System.out.println("修改用户密码");
        User user = userService.findUserByPhone(phone);
        if(user==null){  //没有此用户返回null
            return "";
        }else{   //有此用户则进行修改
            int count = userService.updateUserPasswordByPhone(phone,password);
            return count!=0?"OK":"NO";
        }
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

    @RequestMapping("/login")
    public User login(@RequestParam("phone")String phone,
                      @RequestParam("password")String password){
        System.out.println("登录");
        User user = userService.findByPhoneAndPassword(phone,password);
       return user;
    }

}
