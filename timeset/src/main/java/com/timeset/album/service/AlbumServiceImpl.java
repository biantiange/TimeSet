package com.timeset.album.service;

import com.timeset.album.entity.Album;
import com.timeset.album.mapper.AlbumMapper;
import com.timeset.photo.entity.Photo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName PhotoServiceImpl
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:07
 */
@Service
public class AlbumServiceImpl {
    @Resource
    private AlbumMapper albumMapper;

    //查找全部相册
    public List<Album> findAll(int userId){return albumMapper.findAll(userId);};

    //增加相册
    public int addAlbum(int userId,String theme,String albumName){return albumMapper.addAlbum(userId,theme,albumName);};

    //删除相册
    public int deleteAlbum(int userId,int id){return albumMapper.deleteAlbum(id,userId);};

    //根据ID修改相册名称
    public int updateNameAlbum(int userId,int id, String albumName){return albumMapper.updateNameAlbum(id,userId,albumName);};

    //根据ID修改相册主题
    public int updateThemeAlbum(int userId,int id, String theme){return albumMapper.updateThemeAlbum(id,userId,theme);};
}
