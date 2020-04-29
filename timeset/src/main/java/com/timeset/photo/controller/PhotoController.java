package com.timeset.photo.controller;

import com.timeset.photo.entity.Photo;
import com.timeset.photo.service.PhotoServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    public int addPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam("userId")  int userId, @RequestParam("albumId")  int albumId, @RequestParam("ptime") String ptime,
                        @RequestParam("place")  String place,@RequestParam("describe")  String describe){
        System.out.println("插入图片");
        // 生成新的文件名
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();
        // 保存路径
        String destFileName=request.getServletContext().getRealPath("")+"uploaded"+ File.separator+fileName;
        // 执行保存操作
        File destFile = new File(destFileName);
        if (!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdir();
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photo photo=new Photo();
        photo.setUserId(userId);
        photo.setAlbumId(albumId);
        photo.setPtime(ptime);
        photo.setPdescribe(describe);
        photo.setPath(File.separator+fileName);
        int result=photoService.addPhoto(photo);
        if(result!=0){
            return 0;
        }
        return -1;
    }

    @RequestMapping("/delete")
    public int delete(@RequestParam("id")  int id){
        System.out.println("删除图片");
        if(id!=0) {
            int result=photoService.deletePhoto(id);
            if(result!=0){
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("/findAllByUserId")
    public List<Photo> all(@RequestParam("userId")int userId) {
        System.out.println("查询用户所有图片");
        return photoService.findAll(userId);
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
    public List<Photo> findByTime(@RequestParam("time") String date,@RequestParam("userId")int userId) {
        System.out.println("根据时间查询图片");
        if (userId>0 && date != null && !date.equals("")) {
            return photoService.findByTime(date,userId);
        }
        return photosList;

    }

    @RequestMapping("/find")
    public List<Photo> find(@RequestParam("str") String str,@RequestParam("userId")int userId) {
        System.out.println("根据字符查询图片");
        if (userId>0 && str != null && !str.equals("")) {
            return photoService.findPlaceOrdescribe(str,userId);
        }
        return photosList;
    }


}
