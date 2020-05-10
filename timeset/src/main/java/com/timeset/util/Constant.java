package com.timeset.util;


import com.timeset.user.controller.UserController;

/**
 * @ClassName: Constant
 * @Description: TODO 常量类
 * @Author: szy
 * @Date 2020/4/21
 */
public class Constant {
    //设置APPID/AK/SK

    public static final String APP_ID = "19528543";
    public static final String API_KEY = "v1tzw5uR0S2Q5CsKhC72yfm5";
    public static final String SECRET_KEY = "bVue3fHGditooTKLbGZGCELa1prG3Gr8";
    public static final String HeadImgPath= UserController.class.getClassLoader().getResource("").getPath().split("timeset")[0];

   // public static final String ImgPath= UserController.class.getClassLoader().getResource("").getPath().split("timeset")[0]+"uploaded";
}
