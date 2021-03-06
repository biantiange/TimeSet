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
import com.timeset.util.FileUtil;
import com.timeset.util.MultipartFileToFileUtil;
import com.timeset.util.QiniuUtil;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Resource
    private QiniuUtil qiniuUtil;

    @RequestMapping("/add")
    public int addPhoto(@RequestParam("file") MultipartFile files[], HttpServletRequest request, @RequestParam("userId") int userId, @RequestParam("albumId") int albumId,
                        @RequestParam("describe") String describe, @RequestParam("infor") String infor) {
        System.out.println("插入图片");
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<PhotoJson> jlist = gson.fromJson(infor, new TypeToken<List<PhotoJson>>() {
        }.getType());
        System.out.println("jlist"+jlist);
        String pa = UserController.class.getClassLoader().getResource("").getPath().split("timeset")[0];
        int repeat = 0;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename();
            System.out.println("imgName"+fileName);
//            String dir1 = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";
//            File file1=new File((dir1));
//            if(!file1.exists()){
//                file1.mkdir();
//            }
//            String dir2 = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/"+userId+"/";
//            File file2=new File((dir2));
//            if(!file2.exists()){
//                file2.mkdir();
//            }
//            // 保存路径
//            String destFileName = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + userId + "/" +albumId+"/"+  fileName;
//            //String destFileName1 = request.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
//            System.out.println(destFileName);
//            //System.out.println(destFileName1);
//            //String destFileName=Constant.ImgPath+File.separator+fileName;
//            // 执行保存操作
//            File destFile = new File(destFileName);
////            System.out.println(destFileName);
////            if(destFile.exists()){
////                repeat++;
////            }else {
//            if (!destFile.getParentFile().exists()) {
//                destFile.getParentFile().mkdir();
//                System.out.println("创建目录");
//            }else{
//                System.out.println("目录存在");
//            }
//            if (destFile.exists()) {
//                repeat++;
//            } else {
//                try {
//                    files[i].transferTo(destFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            Photo photo = new Photo();
            if (jlist.get(i).getCity() != null && !jlist.get(i).getCity().equals("")) {
                photo.setCity(jlist.get(i).getCity());
                System.out.println("city"+jlist.get(i).getCity());
            }
            System.out.println("city_ok"+jlist.get(i).getCity());
            if (jlist.get(i).getDistrict() != null && !jlist.get(i).getDistrict().equals("")) {
                photo.setDistrict(jlist.get(i).getDistrict());
            }
            if (jlist.get(i).getPlace() != null && !jlist.get(i).getPlace().equals("")) {
                photo.setPlace(jlist.get(i).getPlace());
            }
            photo.setUserId(userId);
            photo.setAlbumId(albumId);
            if (jlist.get(i).getProvince()!= null && !jlist.get(i).getProvince().equals("")) {
                photo.setProvince(jlist.get(i).getProvince());
            }
            String date = "";
            if (jlist.get(i).getPtime().length() == 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
                date = df.format(new Date());// new Date()为获取当前系统时间
            } else {
                date = jlist.get(i).getPtime();
            }
            photo.setPtime(date);
            String d = null;
            try {
                d = URLDecoder.decode(describe, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (d != null) {
                photo.setPdescribe(d);
            }
            photo.setLatitude(jlist.get(i).getLat());
            photo.setLongitude(jlist.get(i).getLon());
            MultipartFile file = files[i];
            File f = null;
            if (file != null) {
                try {
                    f = MultipartFileToFileUtil.multipartFileToFile(file);
                    String path = qiniuUtil.saveImage(f, fileName);
                    photo.setPath(path);
                    int result = photoService.addPhoto(photo, path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MultipartFileToFileUtil.delteTempFile(f);

//            photo.setPath(+userId + "/" + albumId + "/" + fileName);
//
//            int result = photoService.addPhoto(photo, destFileName);
        }
//        }

//            if (result == 0) {
//                return -1;
//            }
//        }

        return repeat;
    }

    @RequestMapping("/delete")
    public int delete(@RequestParam("photoId") int id) {
        System.out.println("删除图片");
        if (id != 0) {
            int result = photoService.deletePhoto(id);
            if (result != 0) {
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("updatePhotoDescription")
    public int updatePhotoDescription(@RequestParam("newDescribe") String newDescribe, @RequestParam("photoId") int id) {
        System.out.println("修改图片描述");
        if (id != 0) {
            int result = photoService.updatePhotoDescription(newDescribe, id);
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
