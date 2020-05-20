package com.timeset.user.entity;

/**
 * @ClassName user
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:33
 */
public class User {
    private int id;
    private String phone;
    private String password;
    private String user_name;
    private String head_img;

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
