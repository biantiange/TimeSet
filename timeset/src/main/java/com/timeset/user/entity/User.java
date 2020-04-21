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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", user_name='" + user_name + '\'' +
                ", head_img='" + head_img + '\'' +
                '}';
    }
}
