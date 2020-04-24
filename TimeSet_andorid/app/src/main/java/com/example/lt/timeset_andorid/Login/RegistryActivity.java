package com.example.lt.timeset_andorid.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;
import com.mob.MobSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.lt.timeset_andorid.Login.MobUtil.APPKEY;
import static com.example.lt.timeset_andorid.Login.MobUtil.APPSECRET;
import static com.example.lt.timeset_andorid.Login.MobUtil.isSpecialChar;

/**
 * 卢朋娇
 */
public class RegistryActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;

    //点击事件监听器
    private MyListener myListener;
    private ImageView headImg;  // 头像
    private boolean flag=false;   //是否更新了头像

    // alertDialog
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int REQUEST_TAKEPHOTO = 1;   //拍照
    private static final int REQUEST_GALLERY = 2;    //从相册选择

    // 裁剪后图片的宽(X)和高(Y),80 X 80的正方形。
    private static int output_X = 80;
    private static int output_Y = 80;

    private EditText etPhone, etYanzhengma, etPwd1, etPwd;
    private Button btnSubmit, btnGetMsg, btnReturn;  //btnSubmit是next按钮
    private ImageView ivPhone,ivPwd,ivPwd1;    //输入错误与否的图片
    private int i = 30;//计时器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }

        findViews();

        //如果 targetSdkVersion小于或等于22，可以忽略这一步，如果大于或等于23，需要做权限的动态申请：
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);
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
        //注册一个事件回调监听，用于处理SMSSDK接口请求的结果
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
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Log.e("phone焦点","失去");
                    if (!MobUtil.judgePhoneNums(etPhone.getText().toString())) {
                        ivPhone.setBackgroundResource(R.drawable.error);
                    }else{
                        ivPhone.setBackgroundResource(R.drawable.right);
                    }
                }
            }
        });
        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
        });
        etPwd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String str = etPwd1.getText().toString();
                    if (isSpecialChar(str)|| str.length()<8 || str.length()>12) {
                        ivPwd1.setBackgroundResource(R.drawable.error);
                    }else{
                        ivPwd1.setBackgroundResource(R.drawable.right);
                    }
                }
            }
        });
    }

    /*
    初始化控件方法
     */
    public void viewInit() {
        builder = new AlertDialog.Builder(this);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        takePhotoTV = layout.findViewById(R.id.photograph);
        choosePhotoTV = layout.findViewById(R.id.photo);
        cancelTV = layout.findViewById(R.id.cancel);
        //设置监听
        takePhotoTV.setOnClickListener(myListener);
        choosePhotoTV.setOnClickListener(myListener);
        cancelTV.setOnClickListener(myListener);
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
//                if (msg.obj.equals("OK")) {
//                    //Looper.prepare();
//                    Toast.makeText(ForgetPasswordActivity.this, "操作成功", Toast.LENGTH_LONG).show();
//                    finish();
//                } else if (msg.obj.equals("")) {
//                    Toast.makeText(ForgetPasswordActivity.this, "该手机号已被注册了哦", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(ForgetPasswordActivity.this, "操作失败", Toast.LENGTH_LONG).show();
//                }

            }else {
                int i = msg.arg1;
                int i1 = msg.arg2;
                Object o = msg.obj;
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回LoginActivity,然后提示
                } else if (i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    Toast.makeText(RegistryActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //上传头像
                case R.id.iv_headImg:
                    viewInit();
                    break;
                    // 照相
                case R.id.photograph:
                    dialog.dismiss();
                    if (ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                        ActivityCompat.requestPermissions(RegistryActivity.this,
                                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_TAKEPHOTO);

                    } else {
                        choseHeadImageFromCameraCapture();
                    }

                    break;
                //从相册选择
                case R.id.photo:
                    dialog.dismiss();
                    //加载相册
                    if (ContextCompat.checkSelfPermission(RegistryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistryActivity.this,
                                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_GALLERY);

                    } else {
                        choseHeadImageFromGallery();
                    }
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
                    break;

                case R.id.btn_submit:
                    phoneNum = etPhone.getText().toString();
                    Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNum, etYanzhengma.getText().toString());
                    if(ivPhone.getBackground().getConstantState().equals(drawableCs) && ivPwd.getBackground().getConstantState().equals(drawableCs) && ivPwd1.getBackground().getConstantState().equals(drawableCs)){
                        if(etPwd.getText().toString().equals(etPwd1.getText().toString())){
                            //注册
                            Log.e("获取到的信息：",ivPhone+"-"+ivPwd);
//                            MyOkHttp(Constant.IP + "user/register");
                            //finish();
                        }else{
                            Toast.makeText(RegistryActivity.this,"密码与确认密码不相等",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(RegistryActivity.this,"提交失败,请根据输入框的图片提示来修改您的数据,以来得到您想要的效果",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if(intent!=null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        //显示到imageView
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        headImg.setImageBitmap(bitmap);
                        flag = true;
                    }
                }
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImg.setImageBitmap(photo);
            flag = true;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_GALLERY:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)            {
                    Toast.makeText(this, "权限申请成功",Toast.LENGTH_SHORT).show();
                    choseHeadImageFromGallery();
                }else if (grantResults[0]== PackageManager.PERMISSION_DENIED)            {
                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKEPHOTO:   //照相
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)            {
                    Toast.makeText(this, "权限申请成功",Toast.LENGTH_SHORT).show();
                    choseHeadImageFromCameraCapture();
                }else{
                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //修改数据库，在数据库中加入新用户
//    public void MyOkHttp(String url) {
//        if(flag){   //说明更新了头像，就上传他更新的
//            File file = new File(headImg.getDrawable().get)
//        }else{
//            //上传默认的
//
//        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
//        final Call call = okHttpClient.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Message message = new Message();
//                message.what = 4;
//                handler.sendMessage(message);
//                //Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // Toast.makeText(RegisterActivity.this,"操作成功", Toast.LENGTH_SHORT).show();
//                String jsonStr = response.body().string();
//                Log.e("RegisterActivity", "响应：" + jsonStr);
//                // jsonStr = new Gson().fromJson(jsonStr,String.class);
//                Message message = new Message();
//                message.what = 5;
//                message.obj = jsonStr;
//                handler.sendMessage(message);
//            }
//        });
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        finish();
     }
    return super.onKeyDown(keyCode, event);
 }
}
