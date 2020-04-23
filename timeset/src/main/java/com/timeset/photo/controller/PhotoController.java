package com.timeset.photo.controller;

import com.timeset.photo.entity.Photo;
import com.timeset.photo.service.PhotoServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PhotoController
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:06
 */
@RestController
@RequestMapping("/photo")
public class PhotoController {
    List<Photo> photosList = null;
    @Resource
    private PhotoServiceImpl photoService;

    @RequestMapping("/add")
    public void addPhoto(@RequestParam("albumId")  int album_id,@RequestParam("pdescribr")  String pdescribr,@RequestParam("ptime") String ptime,
                         @RequestParam("place")  String place,@RequestParam("path")  String path,@RequestParam("identify")  String identify){
        if(album_id!=0 && pdescribr!=null && !pdescribr.equals("")&& ptime!=null && !ptime.equals("")&& place!=null && !place.equals("")
                && path!=null && !path.equals("")&& identify!=null && !identify.equals(""))
        photoService.addPhoto(album_id,pdescribr,ptime,place,path,identify);
    }

    @RequestMapping("/delete")
    public void delete(@RequestParam("id")  int id){
        if(id!=0) {
            photoService.deletePhoto(id);
        }
    }

    @RequestMapping("/all")
    public List<Photo> all() {
        System.out.println("查询所有图片");
        return photoService.findAll();
    }

    @RequestMapping("/findByAlbum")
    public List<Photo> findByAlbum(@RequestParam("albumId") String albumId) {
        System.out.println("根据相册查询图片");
        if (albumId != null && !albumId.equals("")) {
            return photoService.findByAlbum(albumId);
        }
        return photosList;

    }

    @RequestMapping("/findByTime")
    public List<Photo> findByTime(@RequestParam("time") String date) {
        System.out.println("根据时间查询图片");
        if (date != null && !date.equals("")) {
            return photoService.findByTime(date);
        }
        return photosList;

    }

    @RequestMapping("/find")
    public List<Photo> find(@RequestParam("str") String str) {
        System.out.println("根据字符查询图片");
        if (str != null && !str.equals("")) {
            return photoService.findPlaceOrdescribe(str);
        }
        return photosList;
    }


}
