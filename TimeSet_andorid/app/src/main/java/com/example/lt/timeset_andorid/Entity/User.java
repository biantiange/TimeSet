package com.example.lt.timeset_andorid.Entity;

/**
 * 卢朋娇
 */
public class User {
    private int id;
    private String phone;  //手机号
    private String password;  //密码
    private String user_name;  // 昵称，如果没有设置就是手机号
    private String head_img;  // 头像，如果没有设置就是默认头像 R.drwaable.moren_img

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String userName) {
        this.user_name = userName;
    }

    public String getHeadImg() {
        return head_img;
    }

    public void setHeadImg(String headImg) {
        this.head_img = headImg;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + user_name + '\'' +
                ", headImg='" + head_img + '\'' +
                '}';
    }
}
