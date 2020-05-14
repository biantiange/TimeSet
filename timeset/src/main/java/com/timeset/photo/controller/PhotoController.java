package com.timeset.photo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.timeset.photo.entity.Photo;
import com.timeset.photo.entity.PhotoJson;
import com.timeset.photo.entity.PhotoList;
import com.timeset.photo.service.PhotoServiceImpl;
import com.timeset.user.controller.UserController;
import com.timeset.util.Constant;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.thymeleaf.util.StringUtils.trim;

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
    public int addPhoto(@RequestParam("file") MultipartFile files[], HttpServletRequest request, @RequestParam("userId") int userId, @RequestParam("albumId") int albumId,
                        @RequestParam("city") String city, @RequestParam("district") String district, @RequestParam("place") String place, @RequestParam("describe") String describe, @RequestParam("infor") String infor) {
        System.out.println("插入图片");
        System.out.println(userId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<PhotoJson> jlist = gson.fromJson(infor, new TypeToken<List<PhotoJson>>() {
        }.getType());
        System.out.println(jlist);
        String pa = UserController.class.getClassLoader().getResource("").getPath().split("timeset")[0];
        int repeat = 0;
        for (int i = 0; i < files.length; i++) {
            // 生成新的文件名
//            String fileName = System.currentTimeMillis() + files[i].getOriginalFilename();
            String fileName = files[i].getOriginalFilename();
            String dir1 = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";
            File file1=new File((dir1));
            if(!file1.exists()){
                file1.mkdir();
            }
            String dir2 = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/"+userId+"/";
            File file2=new File((dir2));
            if(!file2.exists()){
                file2.mkdir();
            }
            // 保存路径
            String destFileName = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + userId + "/" +albumId+"/"+  fileName;
            //String destFileName1 = request.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
            System.out.println(destFileName);
            //System.out.println(destFileName1);
            //String destFileName=Constant.ImgPath+File.separator+fileName;
            // 执行保存操作
            File destFile = new File(destFileName);
//            System.out.println(destFileName);
//            if(destFile.exists()){
//                repeat++;
//            }else {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdir();
                System.out.println("创建目录");
            }else{
                System.out.println("目录存在");
            }
            if (destFile.exists()) {
                repeat++;
            } else {
                try {
                    files[i].transferTo(destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Photo photo = new Photo();
                photo.setCity(city);
                photo.setDistrict(district);
                photo.setPlace(place);
                photo.setUserId(userId);
                photo.setAlbumId(albumId);
                String date = "";
                if (jlist.get(i).getPtime().length() == 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
                    date = df.format(new Date());// new Date()为获取当前系统时间
                } else {
                    date = jlist.get(i).getPtime();
                }
                photo.setPtime(date);
                photo.setPdescribe(describe);
                photo.setLatitude(jlist.get(i).getLat());
                photo.setLongitude(jlist.get(i).getLon());
                photo.setPath(+ userId + "/" +albumId+"/"+fileName);

                int result = photoService.addPhoto(photo, destFileName);
            }
        }

//            if (result == 0) {
//                return -1;
//            }
//        }

        return repeat;
    }

    @RequestMapping("/delete")
    public int delete(@RequestParam("id") int id) {
        System.out.println("删除图片");
        if (id != 0) {
            int result = photoService.deletePhoto(id);
            if (result != 0) {
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("/findAllByUserId")
    public List<Photo> all(@RequestParam("userId") int userId) {
        System.out.println("查询用户所有图片");
        return photoService.findAll(userId);
    }

    @RequestMapping("/findByAlbum")
    public String findByAlbum(@RequestParam("albumId") String alId, @RequestParam("userId") String userId) {
        System.out.println("根据相册查询图片");

        int albumId = Integer.parseInt(alId);
        int useId = Integer.parseInt(userId);
        List<PhotoList> lists = null;
        Gson gson = new GsonBuilder().serializeNulls().create();
        lists = photoService.findByAlbum(albumId, useId);
        String gsonString = gson.toJson(lists);
        System.out.println(gsonString);
        return gsonString;

    }

    @RequestMapping("/findByTime")
    public List<Photo> findByTime(@RequestParam("time") String date, @RequestParam("userId") int userId) {

        System.out.println("根据时间查询图片");
        if (userId > 0 && date != null && !date.equals("")) {
            return photoService.findByTime(date, userId);
        }
        return photosList;

    }

    @RequestMapping("/find")
    public Map<String, List<Photo>> find(@RequestParam("str") String str1, @RequestParam("userId") int userId) {
        if (str1 != null) {
            String str = trim(str1);
            System.out.println("根据字符查询图片");
            if (userId > 0 && str != null && !str.equals("")) {
                return photoService.findPlaceOrDescribeOrIdentify(str, userId);
            }
        }
        return null;
    }


}
