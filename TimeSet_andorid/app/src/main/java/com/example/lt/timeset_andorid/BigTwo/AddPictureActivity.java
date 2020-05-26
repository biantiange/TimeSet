package com.example.lt.timeset_andorid.BigTwo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.Entity.PhotoJson;
import com.example.lt.timeset_andorid.R;

import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.GlideEngine;
import com.example.lt.timeset_andorid.util.GpsUtil;
import com.example.lt.timeset_andorid.util.OnRecyclerItemClickListener;
import com.example.lt.timeset_andorid.util.ReadUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.PictureSelectorExternalUtils;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.camera.CustomCameraView;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPictureActivity extends AppCompatActivity {
    private static final int ADD_ACHIEVE = 500;
    private static final int GET_LOCATION = 600;
    private ImageView ivBack;
    //    private TextView upLoad;
    private Button upLoad;
    private RecyclerView rcvImg;
    private EditText etContent;
    private AddPictureRecyclerAdapter adapter;

    private int userId;     // 用户id
    private int albumId;    // 相册id
    private List<String> NWList = new ArrayList<>();//图片经纬度集合
    private List<String> imgPaths = new ArrayList<>();  // 图片本地路径数据源
    private List<PhotoJson> re = new ArrayList<>();
    private List<LocalMedia> result = new ArrayList<>();

    public static final String GET_IMAGES_FROM_ADD = "getImagesFromAdd";
    public static final String GET_IMAGES_FROM_INTENT = "getImagesFromIntent";
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    public static Activity activity;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album_add_img);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }

        EventBus.getDefault().register(this);
        initDatas();
        findView();
        openPhotoSelector(GET_IMAGES_FROM_INTENT);
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectPicture(Map<String, Object> map) {
        switch (map.get("type").toString()) {
            case GET_IMAGES_FROM_ADD:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                this.result = (List<LocalMedia>) map.get("res");
                openPhotoSelector(GET_IMAGES_FROM_ADD);
                break;
        }
    }

    /**
     * 启动图片查看器
     */
    private void openPhotoSelector(String type) {
        // 直接启动图片查看器
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                .isWeChatStyle(false)
                //.openCamera()//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
                .theme(R.style.picture_Sina_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .selectionMode(PictureConfig.MULTIPLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(false)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
//                .isSingleDirectReturn()//PictureConfig.SINGLE模式下是否直接返回
//                .isWeChatStyle()//开启R.style.picture_WeChat_style样式
//                .setPictureStyle()//动态自定义相册主题
//                .setPictureCropStyle()//动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)//相册启动退出动画
                .isCamera(false)//列表是否显示拍照按钮
//                .imageFormat(PictureMimeType.PNG)//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                .maxSelectNum(18)//最大选择数量,默认9张
                .minSelectNum(1)// 最小选择数量
//                .maxVideoSelectNum()//视频最大选择数量
//                .minVideoSelectNum()//视频最小选择数量
//                .videoMaxSecond()// 查询多少秒以内的视频
//                .videoMinSecond()// 查询多少秒以内的视频
                .imageSpanCount(3)//列表每行显示个数
                .openClickSound(false)//是否开启点击声音
                .selectionMedia(result)//是否传入已选图片
//                .recordVideoSecond()//录制视频秒数 默认60s
                .previewEggs(true)//预览图片时是否增强左右滑动图片体验
//                .cropCompressQuality()// 注：已废弃 改用cutOutQuality()
                .isGif(true)//是否显示gif
                .previewImage(true)//是否预览图片
//                .previewVideo()//是否预览视频
//                .enablePreviewAudio()//是否预览音频
                .enableCrop(false)//是否开启裁剪
                .hideBottomControls(false)//显示底部uCrop工具栏
                .compressQuality(100)//图片压缩后输出质量
                .synOrAsy(true)//开启同步or异步压缩
                .isReturnEmpty(true)//未选择数据时按确定是否可以退出
                .isOriginalImageControl(true)//开启原图选项
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)//列表动画效果,AnimationType.ALPHA_IN_ANIMATION、SLIDE_IN_BOTTOM_ANIMATION
                .setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 自定义相机按钮状态,CustomCameraView.BUTTON_STATE_BOTH
                .forResult(new MyResultCallback(type));//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        private String type;

        public MyResultCallback(String type) {
            this.type = type;
        }

        @Override
        public void onResult(List<LocalMedia> result1) {
//            ExifInterface exifInterface=PictureSelectorExternalUtils.getExifInterface(AddPictureActivity.this,result1.get(0).getPath());
//            float[] ll=new float[]{0,0};
//            exifInterface.getLatLong(ll);
//            Log.e("YYYYYYYYYYYYYYY1",ll[0]+" "+ll[1]);
            result = result1;
            int i = 0;
            Log.e("getPPP","nook");
            Toast.makeText(AddPictureActivity.this, "正在获取图片信息请等待", Toast.LENGTH_SHORT).show();
            Log.e("getPPP","ok");
            upLoad.setTextColor(Color.GRAY);
            upLoad.setText("正在获取");
            upLoad.setEnabled(false);
            for (LocalMedia media : result1) {

                String ptime = "";
                String lon = "";
                String lat = "";
                Photo photo1 = getInfo(media.getPath());
                if (photo1 != null) {
                    String s = photo1.getPtime();
                    if (s != null) {
                        String[] s1 = s.split(":");
                        ptime = s1[0] + s1[1] + s1[2].substring(0, 2);
                    }
                    lon = photo1.getLongitude();
                    lat = photo1.getLatitude();
                    //http://api.map.baidu.com/reverse_geocoding/v3/?ak=jN6xIoPtbQK52In8Odt288FimIM4PgiX&mcode=83:E7:2C:1C:95:EB:96:64:B7:1A:54:BA:5C:E0:77:88:0D:37:80:E4;com.example.lt.timeset_andorid&output=json&coordtype=wgs84ll&location=37.42869796923248,122.09564645163738  //GET请求
                }
                PhotoJson photoJson = new PhotoJson();
                photoJson.setPtime(ptime);
                photoJson.setLat(lat);
                photoJson.setLon(lon);
                re.add(photoJson);
                //if (lon!=null && !lon.equals("") && lat!=null && !lat.equals("")) {
                getPlaceByLongitudeAndLatitude(Constant.AK, Constant.MCODE, i);
                //}
                i++;
            }
            Log.e("getPPP",""+re.toString());


//            for (LocalMedia media : result1) {
//                Log.i(TAG, "是否压缩:" + media.isCompressed());
//                Log.i(TAG, "压缩:" + media.getCompressPath());
//                Log.i(TAG, "原图:" + media.getPath());
//                Log.i(TAG, "是否裁剪:" + media.isCut());
//                Log.i(TAG, "裁剪:" + media.getCutPath());
//                Log.i(TAG, "是否开启原图:" + media.isOriginal());
//                Log.i(TAG, "原图路径:" + media.getOriginalPath());
//                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
//                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
//                Log.i(TAG, "Size: " + media.getSize());
            // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
//
//            }
         /*   for (LocalMedia media:result1
                 ) {
                PictureSelectorExternalUtils.getExifInterface();
            }*/

            if (null == adapter) {
                adapter = new AddPictureRecyclerAdapter(result, AddPictureActivity.this, R.layout.item_add_picture_recycler);
                rcvImg.setAdapter(adapter);
            } else {
                adapter.updateRes(result);
            }
        }

        @Override
        public void onCancel() {
            // 取消时
            switch (type) {
                case GET_IMAGES_FROM_INTENT:
                    AddPictureActivity.activity.finish();
                    break;
                case GET_IMAGES_FROM_ADD:
                    result.add(new LocalMedia());
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    }


    private void getPlaceByLongitudeAndLatitude(String ak, String mcode, int i) {
        String lon = re.get(i).getLon();
        String lat = re.get(i).getLat();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Constant.GET_LOCATION_URL + "&location=" + lat + "," + lon + "&ak=" + Constant.AK + "&mcode=" + Constant.MCODE;
//        Log.e("YYYYYYYYYYYYYYY", url);
        Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*  try {
                 *//* Response response = call.execute();
                    String str = response.body().string();
                    Log.e("YYYYYYYYYYYYYYY", str);
                    Map<String, String> map = new HashMap<>();
                    map.put("pi", i + "");
                    map.put("str", str);
                    Message message = new Message();
                    message.what = GET_LOCATION;
                    message.obj = new Gson().toJson(map);
                    myHandler.sendMessage(message);*//*
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                Request request = new Request.Builder().url(url)

                        .build();
                OkHttpClient client = new OkHttpClient();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Log.e("YYYYYYYYYYYYYYY", str);
                        Map<String, String> map = new HashMap<>();
                        map.put("pi", i + "");
                        map.put("str", str);
                        Message message = new Message();
                        message.what = GET_LOCATION;
                        message.obj = new Gson().toJson(map);
                        myHandler.sendMessage(message);


                    }
                });
            }

        }).start();
    }


    // todo: 获取albumId
    private void initDatas() {
        activity = this;
        albumId = getIntent().getExtras().getInt("albumId", -1);
        userId = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0);


        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
    }

    private void findView() {
        rcvImg = findViewById(R.id.recy_add_picture);
        ivBack = findViewById(R.id.iv_add_picture_back);
        upLoad = findViewById(R.id.tv_add_picture_upload);
        etContent = findViewById(R.id.et_add_picture_content);
    }

    private void setListeners() {
        MyListener myListener = new MyListener();
        ivBack.setOnClickListener(myListener);
        upLoad.setOnClickListener(myListener);
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh.getAdapterPosition() == result.size() - 1) {
                    result.remove(result.size() - 1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", GET_IMAGES_FROM_ADD);
                    map.put("res", result);
                    EventBus.getDefault().post(map);
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_add_picture_back:
                    //返回
                    finish();
                    break;
                case R.id.tv_add_picture_upload:
                    // 上传
                    if (albumId == -1) {
                        Toast.makeText(AddPictureActivity.this, "未获取到相册id,添加图片失败!", Toast.LENGTH_SHORT).show();
                    } else {
                        upLoadAllMessage();
                        upLoad.setTextColor(Color.GRAY);
                        upLoad.setText("正在上传");
                        upLoad.setEnabled(false);
                    }
                    break;
            }
        }

    }


    private OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .build();

    private void upLoadAllMessage() {
        //将re转为json字符串
        Gson gson = new Gson();
        String jr = gson.toJson(re);
        Log.e("PPP1",jr.toString());
        MediaType MutilPart_Form_Data = MediaType.parse("application/octet-stream;charset=utf-8");
        MultipartBody.Builder requestBodyBuilder = null;
        try {
            requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("userId", String.valueOf(userId))// 传递表单数据
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("describe", URLEncoder.encode(etContent.getText().toString(), "utf-8"))
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("albumId", String.valueOf(albumId))
                    .addFormDataPart("infor", jr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 可使用for循环添加img file
        if (result.size() > 1) {
            for (int i = 0; i < result.size() - 1; i++) {
                String path = null;
                if (result.get(i).getPath().contains("storage")) {
                    path = result.get(i).getPath();
                } else {
                    path = ReadUtils.getRealPathFromUri(this, Uri.parse(result.get(i).getPath()));
                }
                File file = new File(path);
                requestBodyBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MutilPart_Form_Data, file));
            }
        }
        // 3.3 其余一致
        RequestBody requestBody = requestBodyBuilder.build();
        // todo：ip
        Request request = new Request.Builder().url(Constant.IP + "photo/add")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                client.dispatcher().cancelAll();
                client.connectionPool().evictAll();
                client.newCall(request).enqueue(this);

                Message message = new Message();
                message.what = ADD_ACHIEVE;
                message.obj = "网络错误，上传失败";
                myHandler.sendMessage(message);
//                Log.e("YYYYYYYYYYYYYYY","ff"+e.getMessage()+"-"+e.getCause());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                // todo：获取返回参数
                Message message = new Message();
                message.what = ADD_ACHIEVE;
                message.obj = str;
                myHandler.sendMessage(message);
            }
        });
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ADD_ACHIEVE:
                    if (msg.obj.equals("网络错误，上传失败")) {
                        Toast.makeText(AddPictureActivity.this, msg.obj + "网络原因上传失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddPictureActivity.this, msg.obj + "张图片重复,重复图片未上传", Toast.LENGTH_SHORT).show();
                    }
                    upLoad.setText("上传");
                    upLoad.setTextColor(Color.parseColor("#FFA000"));
                    upLoad.setEnabled(true);
                    finish();
                    break;
                case GET_LOCATION:
                    if (msg.obj != null) {
                        String strr = msg.obj.toString();
                        if (strr != null) {
//                            Log.e("YYYYYYYYYYYYYYY", strr + "");
                            Map<String, String> map = null;
                            map = new Gson().fromJson(strr, new TypeToken<Map<String, String>>() {
                            }.getType());
                            if (map != null) {
                                int i = Integer.parseInt(map.get("pi"));
                                String str = map.get("str");
                                try {
                                    JSONObject result1 = new JSONObject(str);
                                    String status = result1.get("status").toString();
                                    Log.e("YYYYYYYYYYYYYYY", status);
                                    if (status.equals("0")) {
                                        JSONObject result2 = new JSONObject(result1.get("result").toString());
                                        String add = result2.get("formatted_address").toString();
                                        JSONObject result3 = new JSONObject(result2.get("addressComponent").toString());
                                        String province = null;
                                        if (result3.get("province") != null && !result3.get("province").toString().equals("")) {
                                            province = result3.get("province").toString();
                                        }
                                        String city = null;
                                        if (result3.get("city") != null && !result3.get("city").toString().equals("")) {
                                            city = result3.get("city").toString();
                                        }
                                        String district = null;
                                        if (result3.get("district") != null && !result3.get("district").toString().equals("")) {
                                            district = result3.get("district").toString();
                                        }
                                        String town = null;
                                        if (result3.get("town") != null && !result3.get("town").toString().equals("")) {
                                            town = result3.get("town").toString();
                                        }
                                        String street = null;
                                        if (result3.get("street") != null && !result3.get("street").toString().equals("")) {
                                            street = result3.get("street").toString();
                                        }
                                        String place = province + city + district + town + street;
                                        re.get(i).setProvince(province);
                                        re.get(i).setCity(city);
                                        re.get(i).setDistrict(district);
                                        re.get(i).setPlace(place);
                                        upLoad.setTextColor(Color.parseColor("#FFA000"));
                                        upLoad.setText("上传");
                                        upLoad.setEnabled(true);
                                    } else {
                                        upLoad.setTextColor(Color.GRAY);
                                        upLoad.setText("获取失败");
                                        upLoad.setEnabled(false);
                                        Log.e("YYYYYYYYYYYYYYY", "失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (re.get(re.size() - 1).getPlace() != null) {
                        upLoad.setText("上传");
                        upLoad.setTextColor(Color.parseColor("#FFA000"));
                        upLoad.setEnabled(true);
                    }
                    Log.e("setPPP",re.toString());
                    break;
            }
        }
    };

    private Photo getInfo(String path1) {
        Photo photo = null;
        String path = null;
//        Log.e("YYYYYYYYYYYYYYY",path1);
        if (path1.contains("storage")) {
            path = path1;
        } else {
            path = ReadUtils.getRealPathFromUri(this, Uri.parse(path1));
        }
//        Log.e("YYYYYYYYYYYYYYY",path);
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            String guangquan = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            String shijain = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//            Log.e("resultdddd", path + "\t" + shijain);
            String baoguangshijian = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String jiaoju = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String chang = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String kuan = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String moshi = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            String zhizaoshang = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String iso = exifInterface.getAttribute(ExifInterface.TAG_ISO);
            String jiaodu = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            String baiph = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            String altitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
            String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String timestamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            String processing_method = exifInterface.getAttribute(ExifInterface
                    .TAG_GPS_PROCESSING_METHOD);
            //转换经纬度格式
            double lat = score2dimensionality(latitude);
            double lon = score2dimensionality(longitude);
            /**
             * 将wgs坐标转换成百度坐标
             * 就可以用这个坐标通过百度SDK 去获取该经纬度的地址描述
             */
            double[] wgs2bd = GpsUtil.wgs2bd(lat, lon);

//            Log.e("YYYYYYYYYYYYYYY", "shijian:" + shijain + "jingweidu:" + lat + "---" + lon);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("光圈 = " + guangquan + "\n")
                    .append("时间 = " + shijain + "\n")
//                    .append("曝光时长 = " + baoguangshijian + "\n")
//                    .append("焦距 = " + jiaoju + "\n")
//                    .append("长 = " + chang + "\n")
//                    .append("宽 = " + kuan + "\n")
//                    .append("型号 = " + moshi + "\n")
//                    .append("制造商 = " + zhizaoshang + "\n")
//                    .append("ISO = " + iso + "\n")
//                    .append("角度 = " + jiaodu + "\n")
//                    .append("白平衡 = " + baiph + "\n")
//                    .append("海拔高度 = " + altitude_ref + "\n")
//                    .append("GPS参考高度 = " + altitude + "\n")
//                    .append("GPS时间戳 = " + timestamp + "\n")
//                    .append("GPS定位类型 = " + processing_method + "\n")
//                    .append("GPS参考经度 = " + latitude_ref + "\n")
//                    .append("GPS参考纬度 = " + longitude_ref + "\n")
                    .append("GPS经度 = " + lat + "\n")
                    .append("GPS经度 = " + lon + "\n");

            Log.e("YYYYYYYYYYYYYYY", stringBuilder.toString());
            photo = new Photo();

            photo.setPtime(shijain);
            if (latitude != null && longitude != null) {
                photo.setLatitude(String.valueOf(wgs2bd[0]));
                photo.setLongitude(String.valueOf(wgs2bd[1]));
            } else {
                photo.setLatitude(null);
                photo.setLongitude(null);
//                upLoad.setTextColor(Color.GRAY);
//                upLoad.setText("获取失败");
//                upLoad.setEnabled(false);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;

    }


    /**
     * 将 112/1,58/1,390971/10000 格式的经纬度转换成 112.99434397362694格式
     *
     * @param string 度分秒
     * @return 度
     */
    private double score2dimensionality(String string) {
        double dimensionality = 0.0;
        if (null == string) {
            return dimensionality;
        }

        //用 ，将数值分成3份
        String[] split = string.split(",");
        for (int i = 0; i < split.length; i++) {

            String[] s = split[i].split("/");
            //用112/1得到度分秒数值
            double v = Double.parseDouble(s[0]) / Double.parseDouble(s[1]);
            //将分秒分别除以60和3600得到度，并将度分秒相加
            dimensionality = dimensionality + v / Math.pow(60, i);
        }
        return dimensionality;
    }

}
