package com.timeset.photo.service;

import com.baidu.aip.ocr.AipOcr;
import com.timeset.photo.entity.Photo;
import com.timeset.util.Constant;
import com.timeset.util.FileUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static com.timeset.util.Constant.*;


/**
 * @ClassName: TextRecogintion
 * @Description: TODO 文字识别
 * @Author: szy
 * @Date 2020/4/21
 */
@Service
public class TextRecogintion {

    private static AipOcr client = null;

    private TextRecogintion(){}

    public static AipOcr getAipOcr () {
        // 初始化一个AipOcr
        // 调用接口
        if ( null == client){
            synchronized (TextRecogintion.class){
                if (null == client){
                    client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
                    // 可选：设置网络连接参数
                    client.setConnectionTimeoutInMillis(2000);  // 建立连接的超时时间（单位：毫秒）
                    client.setSocketTimeoutInMillis(60000);     // 通过打开的连接传输数据的超时时间（单位：毫秒）

                    // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//                    client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//                    client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

                }
            }
        }
        return client;
    }

    public static String test(String path1,AipOcr client, Photo image){
        //String path =image.getPath();
        String path =path1;

        /**
         * 通用文字识别 : client.basicGeneral();
         * 通用文字识别（高精度）: client.basicAccurateGeneral()
         */
        // 参数为本地图片路径
//        JSONObject res = client.basicAccurateGeneral(path, new HashMap<String, String>());
        // 通用文字识别, 图片参数为远程url图片
//        JSONObject res = client.basicGeneralUrl(path, new HashMap<String, String>());
        // 参数为本地图片二进制数组
        byte[] file = FileUtil.urlToByte(path);
        JSONObject  res = client.basicAccurateGeneral(file, new HashMap<String, String>());
        System.out.println(res.toString(2));
        return (res.toString(2));
    }

}
