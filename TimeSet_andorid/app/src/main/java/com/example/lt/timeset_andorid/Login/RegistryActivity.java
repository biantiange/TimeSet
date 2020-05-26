package com.example.lt.timeset_andorid.Login;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.MobUtil;
import com.example.lt.timeset_andorid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.lt.timeset_andorid.util.MobUtil.APPKEY;
import static com.example.lt.timeset_andorid.util.MobUtil.APPSECRET;
import static com.example.lt.timeset_andorid.util.MobUtil.isSpecialChar;

/**
 * 卢朋娇
 */


public class RegistryActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;

    //点击事件监听器
    private MyListener myListener;
    private ImageView headImg;  // 头像
    private boolean flag=false;   //是否更新了头像
    private String headImgPath = "";

    private Dialog dialog;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;

    private static final int REQUEAT_SELECT_CODE = 100;
    private static final int REQUEST_CAMERA_CODE = 120;
    private static final int CAMERA_OK = 140;
    private static final int READ_OK = 160;
    // 裁剪后图片的宽(X)和高(Y),80 X 80的正方形。
    private static int output_X = 80;
    private static int output_Y = 80;

    private EditText etPhone, etYanzhengma, etPwd1, etPwd,etUserName;
    private Button btnSubmit, btnGetMsg, btnReturn;  //btnSubmit是next按钮
    private ImageView ivPhone,ivPwd,ivPwd1;    //输入错误与否的图片
    private int i = 30;//计时器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        ISNav.getInstance().init((ImageLoader) (context, path, imageView) -> Glide.with(context).load(path).into(imageView));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        //onCreate里注册
        findViews();
        //如果 targetSdkVersion小于或等于22，可以忽略这一步，如果大于或等于23，需要做权限的动态申请：
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        // 2.1  先注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.setAskPermisionOnReadContact(true);
        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);
        //2.2注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void findViews() {
        headImg = findViewById(R.id.iv_headImg);
        myListener = new MyListener();
        headImg.setOnClickListener(myListener);

        okHttpClient = new OkHttpClient();
        etPhone = findViewById(R.id.et_phone);
        etYanzhengma = findViewById(R.id.et_yanzhengma);
        etPwd = findViewById(R.id.et_password);
        etPwd1 = findViewById(R.id.et_password1);
        etUserName = findViewById(R.id.et_name);
        btnGetMsg = findViewById(R.id.btn_yanzhengma);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(myListener);
        btnGetMsg.setOnClickListener(myListener);
        btnReturn = findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(myListener);
        ivPhone = findViewById(R.id.iv_phone);
        ivPwd = findViewById(R.id.iv_pwd);
        ivPwd1 = findViewById(R.id.iv_pwd1);
        //给editText添加内容改变事件
        etPhone.setOnFocusChangeListener((View v, boolean hasFocus)-> {
                    if(!hasFocus){
                        Log.e("phone焦点","失去");
                        if (!MobUtil.judgePhoneNums(etPhone.getText().toString())) {
                            ivPhone.setBackgroundResource(R.drawable.error);
                        }else{
                            ivPhone.setBackgroundResource(R.drawable.right);
                        }
                    }
                }
        );
        etPwd.setOnFocusChangeListener((View v, boolean hasFocus)-> {
                    if(!hasFocus){
                        Log.e("焦点","失去");
                        String str = etPwd.getText().toString();
                        if (isSpecialChar(str)|| str.length()<8 || str.length()>12) {   //含有非法字符
                            ivPwd.setBackgroundResource(R.drawable.error);
                        }else{
                            ivPwd.setBackgroundResource(R.drawable.right);
                        }
                    }
                }
        );
        etPwd1.setOnFocusChangeListener((View v, boolean hasFocus) ->{
                    if(!hasFocus){
                        String str = etPwd1.getText().toString();
                        if (isSpecialChar(str)|| str.length()<8 || str.length()>12) {
                            ivPwd1.setBackgroundResource(R.drawable.error);
                        }else{
                            ivPwd1.setBackgroundResource(R.drawable.right);
                        }
                    }
                }
        );
    }

    /**
     * 短信验证的回调监听
     */
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int i, int i1, Object o) {
            //处理后续操作需传到主线程中执行
            Message message = new Message();
            message.arg1 = i;
            message.arg2 = i1;
            message.obj = o;
            handler.sendMessage(message);
        }
    };

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
        takePhotoTV.setOnClickListener(myListener);
        choosePhotoTV.setOnClickListener(myListener);
        cancelTV.setOnClickListener(myListener);
        dialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == -9) {
                btnGetMsg.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btnGetMsg.setText("获取验证码");
                btnGetMsg.setClickable(true);
                i = 30;
            } else if (msg.what == 4) {
                //网络请求失败
                Toast.makeText(RegistryActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("OK")) {
                    // 操作成功，返回登录界面
                    Toast.makeText(RegistryActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    finish();
                } else if (msg.obj.equals("")) {
                    Toast.makeText(RegistryActivity.this, "该手机号已被注册了哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistryActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }else {
//                Log.e("进入验证","OK");
                if(msg==null || msg.obj==null){
                    return;
                }
//                Log.e("进入验证","1");
                int i = msg.arg1;
                int i1 = msg.arg2;
                Object o = msg.obj;
                if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (i1 == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        Toast.makeText(RegistryActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO 处理错误的结果
                        Log.e("进入验证-获取",""+o);
                        ((Throwable) o).printStackTrace();
                    }
                } else if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (i1 == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        //验证码通过后在进行忘记密码操作
                        Toast.makeText(RegistryActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(PhoneTestActivity.this, "提交验证码成功,可以进行后续操作了", Toast.LENGTH_SHORT).show();
                        //提交其他判断的
                        submit_after();
                    } else {
                        // TODO 处理错误的结果
                        Log.e("进入验证-提交",""+o);
                        Throwable t = (Throwable) o;
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        map1 = new Gson().fromJson(t.getMessage(),new TypeToken<HashMap<String,Object>>(){}.getType());
                        Toast.makeText(RegistryActivity.this,"错误码:"+map1.get("status")+"\n错误信息："+map1.get("description"),Toast.LENGTH_LONG).show();
                        Log.e("提交验证码错误信息",t.getMessage());
                        ((Throwable) o).printStackTrace();
                    }
                }
                // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
            }
        }
    };

    /**
     * 拍照
     */
    private void getPhoto() {
        if (ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistryActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
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
        if (ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistryActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, READ_OK);
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

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //上传头像
                case R.id.iv_headImg:
                    showBottomDialog();
                    break;
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
                case R.id.btn_return:
                    finish();
                    break;
                case R.id.btn_yanzhengma:
                    String phoneNum = etPhone.getText().toString();
                    // 1. 判断手机号是不是11位并且看格式是否合理
                    getCheckCode(phoneNum);
                    break;

                case R.id.btn_submit:
                    Log.e("提交", "点击了");
                    if (etYanzhengma.getText() == null || etYanzhengma.getText().toString().trim().equals("")) {
                        Toast.makeText(RegistryActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", etPhone.getText().toString(), etYanzhengma.getText().toString());
                    break;
            }
        }
    }

    //点击提交后，验证码正确之后的判断方法
    public void submit_after() {
        Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
        Log.e("忘记密码", "获取背景图片了");
        if (ivPhone.getBackground().getConstantState().equals(drawableCs) && ivPwd.getBackground().getConstantState().equals(drawableCs) && ivPwd1.getBackground().getConstantState().equals(drawableCs)) {
            Log.e("背景图片", "相等了");
            if (etPwd.getText().toString().equals(etPwd1.getText().toString())) {
                Log.e("密码与确认密码", "想等了");
                //做忘记密码操作
                MyOkHttp(Constant.IP + "user/forget?phone=" + etPhone.getText().toString() + "&&password=" + etPwd.getText().toString());
//                Toast.makeText(RegistryActivity.this, "忘记密码操作连接数据库", Toast.LENGTH_LONG).show();
            } else {
                Log.e("密码与确认密码", "不相等");
                Toast.makeText(RegistryActivity.this, "密码与确认密码不相等", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("背景图片", "不相等");
            Toast.makeText(RegistryActivity.this, "提交失败,请根据提示修改", Toast.LENGTH_LONG).show();
        }
    }

    //获取验证码
    public void getCheckCode(String phoneNum) {
        if (!MobUtil.judgePhoneNums(phoneNum)) {
            Toast.makeText(RegistryActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2. 通过sdk发送短信验证
        SMSSDK.getVerificationCode("86", phoneNum);
        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
        btnGetMsg.setClickable(false);
        btnGetMsg.setText("重新发送(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
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
                Toast.makeText(RegistryActivity.this, "请手动打开权限", Toast.LENGTH_SHORT).show();
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
        } else if (requestCode == REQUEAT_SELECT_CODE) {
            if (data != null) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra("result");
                RequestOptions ro = new RequestOptions().circleCrop();
                Glide.with(this).load(mSelectPath.get(0)).apply(ro).into(headImg);
                headImgPath = mSelectPath.get(0);
            }
        }
    }

    //修改数据库，在数据库中加入新用户
    public void MyOkHttp(String url) {
//        File file = new File("/storage/emulated/0/song/10086电话录音2018-11-21-21-58-18.amr");
        Request request = null;
        if(flag){   //用户是否改变了头像，如果是，找到对应路径并上传，如果没有就不上传，数据库中头像路径存null，个人中心gilde展示时error设置成R.drawable.moren_img.jpg
            Log.e("注册","用户改变了头像");
            File file = new File(headImgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);//把文件与类型放入请求体
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart("phone", etPhone.getText().toString())//添加表单数据
                    .addFormDataPart("password", etPwd.getText().toString())
                    .addFormDataPart("username", etUserName.getText()==null?etPhone.getText().toString():etUserName.getText().toString());  //如果用户没填昵称，就是手机号
            if (file.exists()){
                builder.addFormDataPart("file", file.getName(), requestBody);//文件名,请求体里的文件
            }
            MultipartBody multipartBody = builder.build();
//            MultipartBody multipartBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("phone", etPhone.getText().toString())//添加表单数据
//                    .addFormDataPart("password", etPwd.getText().toString())
//                    .addFormDataPart("username", etUserName.getText()==null?etPhone.getText().toString():etUserName.getText().toString())  //如果用户没填昵称，就是手机号
//                    .addFormDataPart("file", file.getName(), requestBody)//文件名,请求体里的文件
//                    .build();
            request = new Request.Builder()
//                    .header("Authorization", "Bearer d3e63518-1ba7-4342-b94c-63c8b9b9046b")//添加请求头的身份认证Token
                    .url(url)
                    .post(multipartBody)
                    .build();
        }else{  //用户未改变头像
            Log.e("注册","用户未改变头像");
            FormBody.Builder builder = new FormBody.Builder();
            FormBody body = builder.add("phone",etPhone.getText().toString())
                    .add("password",etPwd.getText().toString())
                    .add("userName",etUserName.getText()==null?etPhone.getText().toString():etUserName.getText().toString())
                    .build();
            request = new Request.Builder()
                    .post(body)
                    .url(url)
                    .build();
        }
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Log.e("RegisterActivity", "响应：" + jsonStr);
                Message message = new Message();
                message.what = 5;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        });
    }

//    // 从本地相册选取图片作为头像
//    private void choseHeadImageFromGallery() {
//        Intent intentFromGallery = new Intent();
//        // 设置文件类型
//        intentFromGallery.setType("image/*");
//        intentFromGallery.setAction(Intent.ACTION_PICK);
//        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
//    }

//    // 启动手机相机拍摄照片作为头像
//    private void choseHeadImageFromCameraCapture() {
//        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent intent) {
//        switch (requestCode) {
//            case CODE_GALLERY_REQUEST:
//                cropRawPhoto(intent.getData());
//                break;
//
//            case CODE_CAMERA_REQUEST:
//                if(intent!=null) {
//                    Bundle bundle = intent.getExtras();
//                    if (bundle != null) {
//                        //显示到imageView
//                        Bitmap bitmap = (Bitmap) bundle.get("data");
//                        headImg.setImageBitmap(bitmap);
//                        flag = true;
//                    }
//                }
//                break;
//
//            case CODE_RESULT_REQUEST:
//                if (intent != null) {
//                    setImageToHeadView(intent);
//                }
//
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, intent);
//    }
//
//
//    /**
//     * 裁剪原始的图片
//     */
//    public void cropRawPhoto(Uri uri) {
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//
//        // aspectX , aspectY :宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        // outputX , outputY : 裁剪图片宽高
//        intent.putExtra("outputX", output_X);
//        intent.putExtra("outputY", output_Y);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CODE_RESULT_REQUEST);
//    }
//
//    /**
//     * 提取保存裁剪之后的图片数据，并设置头像部分的View
//     */
//    private void setImageToHeadView(Intent intent) {
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            headImg.setImageBitmap(photo);
//            flag = true;
//        }
//    }
//
//    /**
//     * 检查设备是否存在SDCard的工具方法
//     */
//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            // 有存储的SDCard
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

//    /**
//     * 权限回调
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_GALLERY:
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)            {
//                    Toast.makeText(this, "权限申请成功",Toast.LENGTH_SHORT).show();
//                    choseHeadImageFromGallery();
//                }else if (grantResults[0]== PackageManager.PERMISSION_DENIED)            {
//                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case REQUEST_TAKEPHOTO:   //照相
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)            {
//                    Toast.makeText(this, "权限申请成功",Toast.LENGTH_SHORT).show();
//                    choseHeadImageFromCameraCapture();
//                }else{
//                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }

//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        finish();
     }
    return super.onKeyDown(keyCode, event);
 }
}
