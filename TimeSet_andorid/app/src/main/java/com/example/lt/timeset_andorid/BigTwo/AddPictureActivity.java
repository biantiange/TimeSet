package com.example.lt.timeset_andorid.BigTwo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.GlideEngine;
import com.example.lt.timeset_andorid.util.OnRecyclerItemClickListener;
import com.luck.picture.lib.PictureSelector;
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPictureActivity extends AppCompatActivity {
    private static final int ADD_ACHIEVE = 500;
    private ImageView ivBack;
    private TextView upLoad;
    private RecyclerView rcvImg;
    private EditText etContent;
    private AddPictureRecyclerAdapter adapter;

    private int userId;     // 用户id
    private int albumId;    // 相册id
    private List<String> imgPaths = new ArrayList<>();  // 图片本地路径数据源
    private List<LocalMedia> result = new ArrayList<>();

    public static final String GET_IMAGES_FROM_ADD = "getImagesFromAdd";
    public static final String GET_IMAGES_FROM_INTENT = "getImagesFromIntent";
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album_add_img);
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
                .isCamera(true)//列表是否显示拍照按钮
                .imageFormat(PictureMimeType.PNG)//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
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
            result = result1;
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
//                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
//            }

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


    // todo: 获取albumId
    private void initDatas() {
        activity = this;
//        albumId = getIntent().getExtras().getInt("albumId",-1);
        albumId = 1;
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
                    upLoadAllMessage();
                    break;
            }
        }
    }

    private OkHttpClient client = new OkHttpClient();

    private void upLoadAllMessage() {
        MediaType MutilPart_Form_Data = MediaType.parse("application/octet-stream;charset=utf-8");
        MultipartBody.Builder requestBodyBuilder = null;
        try {
            requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("userId", userId + "")// 传递表单数据
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("content", URLEncoder.encode(etContent.getText().toString(), "utf-8"))
                    .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                    .addFormDataPart("albumId", albumId + "");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 可使用for循环添加img file
        if (result.size() > 1) {
            for (int i = 0; i < result.size() - 1; i++) {
                File file = new File(result.get(i).getPath());
                requestBodyBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MutilPart_Form_Data, file));
            }
        }
        // 3.3 其余一致
        RequestBody requestBody = requestBodyBuilder.build();
        // todo：ip
        Request request = new Request.Builder().url(Constant.CON_ADD_IMAGE_IP)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // todo：获取返回参数
                Message message = new Message();
                message.what = ADD_ACHIEVE;
                message.obj = response.body().string();
                myHandler.sendMessage(message);
            }
        });
    }
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ADD_ACHIEVE:
                    Toast.makeText(AddPictureActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
}
