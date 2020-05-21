package com.timeset.photo.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.ocr.AipOcr;
import com.google.gson.Gson;
import com.timeset.photo.entity.Photo;
import com.timeset.photo.entity.PhotoList;
import com.timeset.photo.mapper.PhotoMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

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
    private AipOcr aipOcr = TextRecogintion.getAipOcr();
    private AipImageClassify aipImageClassify = ImageRecogintion.getAipImageClassify();

    public int addPhoto(Photo image,String path) {
        // 1. 识别生成image的文字信息
        String text = TextRecogintion.test(path,aipOcr, image);
        JSONObject textObj = new JSONObject(text);
        System.out.println(textObj.names());
        List<Map<String, String>> textList = new Gson().fromJson(textObj.get("words_result").toString(), List.class);
        System.out.println("获取的识别文本信息");
        StringBuilder tex = new StringBuilder("[");
        for (int i = 0; i < textList.size(); i++) {
            tex.append("\""+textList.get(i).get("words")+"\",");
        }
        tex.append("]");

      /* List<String> listTex = new Gson().fromJson(new String(tex),List.class);
       System.out.println("生成链表"+listTex.toString());*/
        System.out.println("生成字符串"+new String(tex));
        System.out.println(tex);
        //2. 识别生成image的物体和场景信息
        String description = ImageRecogintion.getAdvancedGeneral(path,aipImageClassify, image);
        JSONObject desObj = new JSONObject(description);
        List<Map<String, Object>> desList = new Gson().fromJson(desObj.get("result").toString(), List.class);
        System.out.println("获取的识别物体和场景信息");
        StringBuilder des = new StringBuilder("[");
        for (int i = 0; i < desList.size(); i++) {
            if (Double.parseDouble(desList.get(i).get("score").toString()) > 0.01) {
                des.append("\""+desList.get(i).get("keyword")+"\",");
            }
        }
        des.deleteCharAt(des.length()-1);
        des.append("]");
       // List<String> listDec = new Gson().fromJson(new String(des),List.class);
       // System.out.println("生成链表"+listDec.toString());
        System.out.println("生成字符串"+new String(des));
        image.setIdentify(new String(tex)+new String(des));
        System.out.println("图片信息:"+image.toString());
        int result=photoMapper.addPhoto(image);
        if(result!=0){
            return 0;
        }
        return -1;
    }

    public int deletePhoto(int id){

        String destFileName = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + photoMapper.findPhotoNameById(id).getPath().trim();
        File file = new File(destFileName);
        file.delete();
        return photoMapper.deletePhoto(id);
    }

    public int updatePhotoDescription(String describe,int id){ return photoMapper.updatePhotoDescription(describe,id);}

    public List<Photo> findAll(int userId){return photoMapper.findAll(userId);};

    public List<PhotoList> findByAlbum(int albumId, int userId){return photoMapper.findByAlbum(albumId,userId);};

    public List<Photo> findByTime(String date,int userId){return photoMapper.findByTime(date,userId);};

    public Map<String, List<Photo>> findPlaceOrDescribeOrIdentify(String str,int userId){
        Map<String,List<Photo>> map1=new HashMap<>();
        List<Photo> photos1 =photoMapper.findPlace(str,userId);
        List<Photo> photos2 =photoMapper.findDescribe(str,userId);
        List<Photo> photos3=photoMapper.findIdentify(str,userId);
        if(photos1.size()>0){
            map1.put("place",photos1);
        }
        if(photos2.size()>0){
            map1.put("contain",photos2);
        }
        if(photos3.size()>0){
            map1.put("identify",photos3);
        }



//        lists.add(map1);
//        Map<String,List<Photo>> map2=new HashMap<>();

//        lists.add(map2);
//        if(photos1.size()!=0){
//            for(Photo p:photos1){
//                photoAll.add(p);
//            }
//        }
//        if(photos2.size()!=0){
//            for(Photo p:photos2){
//                photoAll.add(p);
//            }
//        }
//        if(photoAll!=null){
//            photos=new ArrayList(photoAll);
//        }
        return map1;
    }
}
