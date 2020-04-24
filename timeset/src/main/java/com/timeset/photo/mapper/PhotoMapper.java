package com.timeset.photo.mapper;

import com.timeset.photo.entity.Photo;

import java.util.List;

/**
 * @ClassName PhotoMapper
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:07
 */
public interface PhotoMapper {

    public int addPhoto(int album_id,String pdescribe,String ptime,String place,String path,String identify);

    public int deletePhoto(int id);

    //查找全部照片
    public List<Photo> findAll();

    //根据相册查询照片
    public List<Photo> findByAlbum(String albumId);

    //根据时间查询照片
    public List<Photo> findByTime(String ptime);

    //根据地点查询照片
    public List<Photo> findPlace(String place);

    //根据描述查询照片
    public List<Photo> findDescribe(String pdescribe);
}
