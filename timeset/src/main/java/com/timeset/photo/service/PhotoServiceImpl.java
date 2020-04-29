package com.timeset.photo.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.ocr.AipOcr;
import com.google.gson.Gson;
import com.timeset.photo.entity.Photo;
import com.timeset.photo.mapper.PhotoMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public int addPhoto(Photo image) {
        // 1. 识别生成image的文字信息
        String text = TextRecogintion.test(aipOcr, image);
        JSONObject textObj = new JSONObject(text);
        System.out.println(textObj.names());
        List<Map<String, String>> textList = new Gson().fromJson(textObj.get("words_result").toString(), List.class);
        System.out.println("获取的识别文本信息");
        StringBuilder tex = new StringBuilder("[");
        for (int i = 0; i < textList.size(); i++) {
            tex.append("\""+textList.get(i).get("words")+"\",");
        }
        tex.deleteCharAt(tex.length()-1);
        tex.append("]");
        List<String> listTex = new Gson().fromJson(new String(tex),List.class);
        System.out.println("生成链表"+listTex.toString());
        System.out.println("生成字符串"+new String(tex));
        //2. 识别生成image的物体和场景信息
        String description = ImageRecogintion.getAdvancedGeneral(aipImageClassify, image);
        JSONObject desObj = new JSONObject(description);
        List<Map<String, Object>> desList = new Gson().fromJson(desObj.get("result").toString(), List.class);
        System.out.println("获取的识别物体和场景信息");
        StringBuilder des = new StringBuilder("[");
        for (int i = 0; i < desList.size(); i++) {
            if (Double.parseDouble(desList.get(i).get("score").toString()) > 0.5) {
                des.append("\""+desList.get(i).get("keyword")+"\",");
            }
        }
        des.deleteCharAt(des.length()-1);
        des.append("]");
        List<String> listDec = new Gson().fromJson(new String(des),List.class);
        System.out.println("生成链表"+listDec.toString());
        System.out.println("生成字符串"+new String(des));
        image.setIdentify(new String(tex)+new String(des));
        //image.setPdescribe(new String(tex)+new String(des));
        System.out.println("图片信息:"+image.toString());
        int result=photoMapper.addPhoto(image);
        if(result!=0){
            return 0;
        }
        return -1;
    }

    public int deletePhoto(int id){return photoMapper.deletePhoto(id);};

    public List<Photo> findAll(int userId){return photoMapper.findAll(userId);};

    public List<Photo> findByAlbum(String albumId){return photoMapper.findByAlbum(albumId);};

    public List<Photo> findByTime(String date,int userId){return photoMapper.findByTime(date,userId);};

    public List<Photo> findPlaceOrdescribe(String str,int userId){
        Set<Photo> photoAll=new HashSet<>();
        List photos=null;
        List<Photo> photos1 =photoMapper.findPlace(str,userId);
        List<Photo> photos2 =photoMapper.findDescribe(str,userId);
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
