package com.example.lt.timeset_andorid.Login;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;
import com.mob.MobSDK;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.lt.timeset_andorid.Login.MobUtil.APPKEY;
import static com.example.lt.timeset_andorid.Login.MobUtil.APPSECRET;
import static com.example.lt.timeset_andorid.Login.MobUtil.isSpecialChar;

/**
 * 卢朋娇
 */
public class PhoneTestActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private EditText etPhone, etYanzhengma, etPwd1, etPwd;
    private Button btnSubmit, btnGetMsg, btnReturn;  //btnSubmit是next按钮
    private ImageView ivPhone,ivPwd,ivPwd1;    //输入错误与否的图片
    private int i = 30;//计时器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
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
//        intent = getIntent();
        okHttpClient = new OkHttpClient();
        etPhone = findViewById(R.id.et_phone);
        etYanzhengma = findViewById(R.id.et_yanzhengma);
        etPwd = findViewById(R.id.et_password);
        etPwd1 = findViewById(R.id.et_password1);
        btnGetMsg = findViewById(R.id.btn_yanzhengma);
        btnSubmit = findViewById(R.id.btn_submit);
        MyListener myListener = new MyListener();
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
                Toast.makeText(PhoneTestActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("OK")) {
                    Toast.makeText(PhoneTestActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    finish();
                } else if (msg.obj.equals("")) {
                    Toast.makeText(PhoneTestActivity.this, "该手机号已被注册了哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PhoneTestActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }

            }else {
                int i = msg.arg1;
                int i1 = msg.arg2;
                Object o = msg.obj;
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回LoginActivity,然后提示
                } else if (i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    Toast.makeText(PhoneTestActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String phoneNum = etPhone.getText().toString();
            switch (v.getId()) {
                case R.id.btn_return:
                    finish();
                    break;
                case R.id.btn_yanzhengma:
                    // 1. 判断手机号是不是11位并且看格式是否合理
                    if (!MobUtil.judgePhoneNums(phoneNum)) {
                        Toast.makeText(PhoneTestActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
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
                    Log.e("提交", "点击了");
                    Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNum, etYanzhengma.getText().toString());
                    Log.e("忘记密码","获取背景图片了");
                    if(ivPhone.getBackground().getConstantState().equals(drawableCs) && ivPwd.getBackground().getConstantState().equals(drawableCs) && ivPwd1.getBackground().getConstantState().equals(drawableCs)){
                        Log.e("背景图片","相等了");
                        if(etPwd.getText().toString().equals(etPwd1.getText().toString())){
                            Log.e("密码与确认密码","想等了");
//                            if(getIntent().getIntExtra("flag",0)==1){
                                //做注册操作
//                                MyOkHttp(Constant.IP+"user/register?phoneNumber="+ etPhone.getText().toString() + "&&password=" + etPwd.getText().toString());
//                            }else{
                                //做忘记密码操作
                                MyOkHttp(Constant.IP+"user/forget?phoneNumber="+ etPhone.getText().toString() + "&&password=" + etPwd.getText().toString());
//                            }
                        }else{
                            Log.e("密码与确认密码","不相等");
                            Toast.makeText(PhoneTestActivity.this,"密码与确认密码不相等",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Log.e("背景图片", "不相等");
                        Toast.makeText(PhoneTestActivity.this,"提交失败,请根据输入框的图片提示来修改您的数据,以来得到您想要的效果",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    //修改数据库，在数据库中加入新用户
    public void MyOkHttp(String url) {
        Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
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

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
