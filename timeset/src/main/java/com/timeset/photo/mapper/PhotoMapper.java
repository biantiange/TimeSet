package com.timeset.photo.mapper;

import com.timeset.photo.entity.Photo;
import com.timeset.photo.entity.PhotoList;

import java.util.List;

/**
 * @ClassName PhotoMapper
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:07
 */
public interface PhotoMapper {

    public int addPhoto(Photo photo);

    public int deletePhoto(int id);

    //查找全部照片
    public List<Photo> findAll(int UserId);

    //根据相册查询照片
    public List<PhotoList> findByAlbum(int albumId, int uerId);

    //根据时间查询照片
    public List<Photo> findByTime(String ptime,int userId);

    //根据地点查询照片
    public List<Photo> findPlace(String place,int userId);

    //根据描述查询照片
    public List<Photo> findIdentify(String identify,int userId);

    //根据描述查询照片
    public List<Photo> findDescribe(String pdescribe,int userId);
}
