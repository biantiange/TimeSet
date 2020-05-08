package com.timeset.photo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timeset.photo.entity.Photo;
import com.timeset.photo.entity.PhotoList;
import com.timeset.photo.service.PhotoServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public int addPhoto(@RequestParam("file") MultipartFile files[], HttpServletRequest request, @RequestParam("userId")  int userId, @RequestParam("albumId")  int albumId, @RequestParam("ptime") String ptime,
                        @RequestParam("place")  String place,@RequestParam("describe")  String describe){
        System.out.println("插入图片");
        if(files!=null && files.length>=1){
            for(MultipartFile file:files){
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
                if(result==0){
                    return -1;
                }
            }
        }
        return 0;
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
    public String findByAlbum(@RequestParam("albumId") String alId,@RequestParam("userId") String userId) {
        System.out.println("根据相册查询图片");

        int albumId= Integer.parseInt(alId);
        int useId= Integer.parseInt(userId);
        List<PhotoList> lists=null;
        Gson gson=new GsonBuilder().serializeNulls().create();
        lists=photoService.findByAlbum(albumId,useId);
        String gsonString=gson.toJson(lists);
        System.out.println(gsonString);
        return gsonString;

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
    public Map<String, List<Photo>> find(@RequestParam("str") String str, @RequestParam("userId")int userId) {
        System.out.println("根据字符查询图片");
        if (userId>0 && str != null && !str.equals("")) {
            return photoService.findPlaceOrDescribeOrIdentify(str,userId);
        }
        return null;
    }


}
