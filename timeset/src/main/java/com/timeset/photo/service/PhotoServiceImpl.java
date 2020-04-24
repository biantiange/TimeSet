package com.timeset.photo.service;

import com.timeset.photo.entity.Photo;
import com.timeset.photo.mapper.PhotoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName PhotoServiceImpl
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:07
 */
@Service
public class PhotoServiceImpl {
    @Resource
    private PhotoMapper photoMapper;

    public int addPhoto(int album_id,String pdescribe,String ptime,String place,String path,String identify){return photoMapper.addPhoto(album_id,pdescribe,ptime,place,path,identify);};

    public int deletePhoto(int id){return photoMapper.deletePhoto(id);};

    public List<Photo> findAll(){return photoMapper.findAll();};

    public List<Photo> findByAlbum(String albumId){return photoMapper.findByAlbum(albumId);};

    public List<Photo> findByTime(String date){return photoMapper.findByTime(date);};

    public List<Photo> findPlaceOrdescribe(String str){
        Set<Photo> photoAll=new HashSet<>();
        List photos=null;
        List<Photo> photos1 =photoMapper.findPlace(str);
        List<Photo> photos2 =photoMapper.findDescribe(str);
        if(photos1.size()!=0){
            for(Photo p:photos1){
                photoAll.add(p);
            }
        }
        if(photos2.size()!=0){
            for(Photo p:photos2){
                photoAll.add(p);
            }
        }
        if(photoAll!=null){
            photos=new ArrayList(photoAll);
        }
        return photos;
    }
}
