package com.timeset.photo.service;


import com.baidu.aip.imageclassify.AipImageClassify;
import com.timeset.photo.entity.Photo;
import com.timeset.util.Constant;
import com.timeset.util.FileUtil;
import org.json.JSONObject;


import java.io.IOException;
import java.util.HashMap;

import static com.timeset.util.Constant.*;


/**
 * @ClassName: ImageRecogintion
 * @Description: TODO 图像识别
 * @Author: szy
 * @Date 2020/4/21
 */
public class ImageRecogintion {
    private static AipImageClassify client;

    private ImageRecogintion() {
    }

    public static AipImageClassify getAipImageClassify() {
        if (client == null) {
            synchronized (ImageRecogintion.class) {
                if (null == client) {
                    client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
                    // 可选：设置网络连接参数
                    client.setConnectionTimeoutInMillis(2000);  // 连接超时时间
                    client.setSocketTimeoutInMillis(60000);     // 请求超时时间
                }
            }
        }
        return client;
    }

    public static String getAdvancedGeneral(String path1, AipImageClassify client, Photo image) {
//            String path =image.getPath();
        String path = path1;
        // 本地文件路径

        /**
         * 通用物体和场景识别高级版: client.advancedGeneral()
         */
        byte[] file = FileUtil.urlToByte(path);
        JSONObject res = client.advancedGeneral(file, new HashMap<String, String>());
        System.out.println(res.toString(2));
        return res.toString(2);
    }
}
