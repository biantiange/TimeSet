package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

import com.example.lt.timeset_andorid.Entity.Photo;

import java.util.List;

import androidx.annotation.NonNull;
/**
 * @ClassName PhotoList
 * @Description 中间表
 * @Author 赵宁
 * @Date 2020-05-03
 */


public class PhotoList {
    private String ptime;
    private List<Photo> photoLists;

    public String getPtime() {
        return ptime;
    }
    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
    public List<Photo> getPhotoList() {
        return photoLists;
    }
    public void setPhotoList(List<Photo> photoList) {
        this.photoLists = photoList;
    }

    @Override
    public String toString() {
        return "PhotoList{" +
                "ptime='" + ptime + '\'' +
                ", photoLists=" + photoLists +
                '}';
    }
}
