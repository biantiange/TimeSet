package com.example.lt.timeset_andorid.Person;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.InputTextDialog;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SkySong
 */
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener{
    private OkHttpClient okHttpClient;

    private InputTextDialog inputTextDialog;

    private Dialog dialog;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;

    private TextView nickName;
    private TextView password;
    private TextView phoneNumber;
    private ImageView headImg;
    private String headImgPath = "";

    private static final int REQUEAT_SELECT_CODE = 100;
    private static final int REQUEST_CAMERA_CODE = 120;
    private static final int CAMERA_OK = 140;
    private static final int READ_OK = 160;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ISNav.getInstance().init((ImageLoader) (context, path, imageView) -> Glide.with(context).load(path).into(imageView));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        //实例化输入框
        inputTextDialog = new InputTextDialog(PersonalActivity.this,R.style.dialog_center);
        //findViews
        findViews();
        SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
        nickName.setText(share.getString("userName","error_name"));
        phoneNumber.setText(share.getString("phone","error_phone"));
        phone = share.getString("phone","error_phone");
    }

    private void findViews() {
        //OkHttpClient
        okHttpClient = new OkHttpClient();
        //TextView
        nickName = findViewById(R.id.personal_name);
        password = findViewById(R.id.personal_password);
        phoneNumber = findViewById(R.id.personal_phone);
        //ImageView
        headImg = findViewById(R.id.personal_iv_headImg);

    }

    public void modifyClick(View view) {
        switch (view.getId()){
            case R.id.modify_nickname://修改昵称
                inputTextDialog.setHint("请输入新的昵称");
                inputTextDialog.setMaxNumber(10);//最长十个字
                inputTextDialog.show();
                inputTextDialog.setmOnTextSendListener(msg -> {
                    nickName.setText(msg);
                    //数据库修改
                    personalOKHttP(Constant.IP + "user/updateUserName?phone=" + phoneNumber.getText().toString() + "&&userName="+msg);
                });

                break;
            case R.id.modify_password://修改密码
                //跳转到修改密码的界面
                Intent intent = new Intent(PersonalActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_phoneNumber://修改手机号
                Intent intent1 = new Intent(PersonalActivity.this,ModifyPhoneNumberActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_return_personal://退出（😂偷懒了，就写这吧）
                finish();
                break;
            case R.id.personal_iv_headImg:
                showBottomDialog();
                break;
        }
    }

    /**
     * 初始化dialog
     */
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.custom_dialog_choose_img, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        takePhotoTV = dialog.findViewById(R.id.photograph);
        choosePhotoTV = dialog.findViewById(R.id.photo);
        cancelTV = dialog.findViewById(R.id.cancel);
        //设置监听
        takePhotoTV.setOnClickListener(this::onClick);
        choosePhotoTV.setOnClickListener(this::onClick);
        cancelTV.setOnClickListener(this::onClick);
        dialog.show();
    }

    private void personalOKHttP(String url) {
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败 error
                e.printStackTrace();
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功 succeed ，，获取返回数据时 回调此方法
                String jsonStr = response.body().string();
                Message message = new Message();
                message.what = 5;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            if (msg.what == 4) {
                //网络请求失败
                Toast.makeText(PersonalActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("0")) {//姓名
                    // 操作成功，返回个人界面
                    Toast.makeText(PersonalActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor= share.edit();
                    editor.putString("userName",nickName.getText().toString());
                    editor.commit();
                }  else {
                    Toast.makeText(PersonalActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }else if(msg.what== 6){//头像
                //网络请求结果
                if (msg.obj.equals("0")) {
                    // 操作成功，返回个人界面
                    Toast.makeText(PersonalActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor= share.edit();
                    editor.putString("headImg",headImgPath);
                    editor.commit();
                }  else {
                    Toast.makeText(PersonalActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    /**
     * 拍照
     */
    private void getPhoto() {
        if (ContextCompat.checkSelfPermission(PersonalActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
        } else {
            ISCameraConfig config1 = new ISCameraConfig.Builder()
                    .needCrop(true) // 裁剪
                    .cropSize(1, 1, 200, 200)
                    .build();
            ISNav.getInstance().toCameraActivity(this, config1, REQUEST_CAMERA_CODE);
        }
    }

    /**
     * 获取图片
     */
    private void selectPhoto() {
        if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA}, READ_OK);
        } else {
            ISListConfig config = new ISListConfig.Builder()
                    // 是否多选, 默认true
                    .multiSelect(false)
                    // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                    .rememberSelected(true)
                    // “确定”按钮背景色
                    .btnBgColor(Color.GRAY)
                    // “确定”按钮文字颜色
                    .btnTextColor(Color.BLUE)
                    // 使用沉浸式状态栏
                    .statusBarColor(Color.parseColor("#3F51B5"))
                    // 返回图标ResId
                    .backResId(R.drawable.abc_action_bar_item_background_material)
                    // 标题
                    .title("选择图片")
                    // 标题文字颜色
                    .titleColor(Color.WHITE)
                    // TitleBar背景色
                    .titleBgColor(Color.parseColor("#3F51B5"))
                    // 裁剪大小。needCrop为true的时候配置
                    .cropSize(1, 1, 200, 200)
                    .needCrop(true)
                    // 第一个是否显示相机，默认true
                    .needCamera(false)
                    // 最大选择图片数量，默认9
                    .maxNum(1)
                    .build();
            // 跳转到图片选择器
            ISNav.getInstance().toListActivity(this, config, REQUEAT_SELECT_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_OK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                ISCameraConfig config1 = new ISCameraConfig.Builder()
                        .needCrop(true) // 裁剪
                        .cropSize(1, 1, 200, 200)
                        .build();
                ISNav.getInstance().toCameraActivity(this, config1, REQUEST_CAMERA_CODE);
            } else{
                //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                Toast.makeText(PersonalActivity.this, "请手动打开权限", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_OK){
            ISListConfig config = new ISListConfig.Builder()
                    // 是否多选, 默认true
                    .multiSelect(false)
                    // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                    .rememberSelected(true)
                    // “确定”按钮背景色
                    .btnBgColor(Color.GRAY)
                    // “确定”按钮文字颜色
                    .btnTextColor(Color.BLUE)
                    // 使用沉浸式状态栏
                    .statusBarColor(Color.parseColor("#3F51B5"))
                    // 返回图标ResId
                    .backResId(R.drawable.abc_action_bar_item_background_material)
                    // 标题
                    .title("选择图片")
                    // 标题文字颜色
                    .titleColor(Color.WHITE)
                    // TitleBar背景色
                    .titleBgColor(Color.parseColor("#3F51B5"))
                    // 裁剪大小。needCrop为true的时候配置
                    .cropSize(1, 1, 200, 200)
                    .needCrop(true)
                    // 第一个是否显示相机，默认true
                    .needCamera(false)
                    // 最大选择图片数量，默认9
                    .maxNum(1)
                    .build();
            // 跳转到图片选择器
            ISNav.getInstance().toListActivity(this, config, REQUEAT_SELECT_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result"); // 图片地址
            RequestOptions ro = new RequestOptions().circleCrop();
            Glide.with(this).load(path).apply(ro).into(headImg);
            headImgPath = path;
            headOkHttp(Constant.IP + "user/updateUserImg");

        } else if (requestCode == REQUEAT_SELECT_CODE) {
            if (data != null) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra("result");
                RequestOptions ro = new RequestOptions().circleCrop();
                Glide.with(this).load(mSelectPath.get(0)).apply(ro).into(headImg);
                headImgPath = mSelectPath.get(0);
                headOkHttp(Constant.IP + "user/updateUserImg");
            }
        }
    }

    public void headOkHttp(String url){
        Log.e("注册","用户改变了头像");
        File file = new File(headImgPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);//把文件与类型放入请求体
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("phone",phone);


        if (file.exists()){
            builder.addFormDataPart("file", file.getName(), requestBody);//文件名,请求体里的文件
        }
        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败 error
                e.printStackTrace();
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功 succeed ，，获取返回数据时 回调此方法
                String jsonStr = response.body().string();
                Message message = new Message();
                message.what = 6;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            // 照相
            case R.id.photograph:
                dialog.dismiss();
                getPhoto();
                break;
            //从相册选择
            case R.id.photo:
                dialog.dismiss();
                selectPhoto();
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
        }
    }
}
