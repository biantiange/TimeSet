package com.timeset.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName QiniuUtil
 * @Description
 * @Author szy
 * @Date 2020-05-20 16:13
 */
@Service
public class QiniuUtil {


    // 设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey = "25FkeF0e8nUJgfAwOuow9K3a2Z6FBgVXjz9BFb_s";
    String secretKey = "k9gCjbz51ABA4gx90juQSOMibsKFVOdPc9S3Pqpy";
    String bucket = "timeset";


    // 密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
    Configuration cfg = new Configuration(Zone.zone2());
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucket);
    }


    public String saveImage(File file, String fileName) throws IOException {
        try {
            int dotPos = fileName.lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = fileName.substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isFileAllowed(fileExt)) {
                return null;
            }
            String randName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            Response res = uploadManager.put(file, randName, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return "http://qamf9zffk.bkt.clouddn.com/" + JSONObject.parseObject(res.bodyString()).get("key").toString();
            } else {
                System.out.println("七牛异常1:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            System.out.println("七牛异常2:" + e.getMessage());
            return null;
        }
    }


}
