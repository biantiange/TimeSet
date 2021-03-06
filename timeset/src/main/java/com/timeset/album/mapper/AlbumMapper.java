package com.timeset.album.mapper;


import com.timeset.album.entity.Album;

import java.util.List;

/**
 * @ClassName PhotoMapper
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:07
 */
public interface AlbumMapper {

    //查找全部相册
    public List<Album> findAll(int userId);

    //增加相册
    public int addAlbum(Album album);

    //删除相册
    public int deleteAlbum(int id);

    //根据ID修改相册名称
    public int updateNameAlbum(int id,int userId, String albumName);

    //根据ID修改相册主题
    public int updateThemeAlbum(int id,int userId, String theme);




}
