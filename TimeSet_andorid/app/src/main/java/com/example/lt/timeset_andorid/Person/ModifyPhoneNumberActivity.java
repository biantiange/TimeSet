package com.example.lt.timeset_andorid.Person;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;

import com.example.lt.timeset_andorid.util.MobUtil;
import com.mob.MobSDK;


import java.io.IOException;

import static com.example.lt.timeset_andorid.util.MobUtil.APPKEY;
import static com.example.lt.timeset_andorid.util.MobUtil.APPSECRET;


/**
 * SkySong
 */
public class ModifyPhoneNumberActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    private int userId;

    private EditText phone;
    private EditText testNumber;
    private Button getTestNumber;
    private ImageView phoneImg;
    private int i = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_number);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
        userId = share.getInt("id",0);

        findViews();
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
        //OkHttpClient
        okHttpClient = new OkHttpClient();
        //EditText
        phone = findViewById(R.id.modify_et_phone);
        testNumber = findViewById(R.id.modify_et_yanzhengma);
        //Button
        getTestNumber = findViewById(R.id.modify_btn_yanzhengma);
        //ImageView
        phoneImg = findViewById(R.id.modify_iv_phone);
        //EditText 内容判断—— when edit text lose focus
        phone.setOnFocusChangeListener((View v,boolean hasFocus)->{
            if (!hasFocus) {
                String phoneStr = phone.getText().toString();
                if (!MobUtil.judgePhoneNums(phoneStr)){
                    phoneImg.setBackgroundResource(R.drawable.error);
                }else {
                    phoneImg.setBackgroundResource(R.drawable.right);
                }
            }
        });
    }

    public void phoneNumberClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_phone://返回
                finish();
                break;

            case R.id.modify_btn_yanzhengma://验证码
                String phoneNum = phone.getText().toString();
                // 1. 判断手机号是不是11位并且看格式是否合理
                if (!MobUtil.judgePhoneNums(phoneNum)) {
                    Toast.makeText(ModifyPhoneNumberActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNum);
                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                getTestNumber.setClickable(false);
                getTestNumber.setText("重新发送(" + i + ")");
                new Thread(() -> {
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
                }).start();
                break;
            case R.id.phoneNum_sure://确认
                phoneNum = phone.getText().toString();
                Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNum, testNumber.getText().toString());
                if(phoneImg.getBackground().getConstantState().equals(drawableCs)){
                    phoneOkHttp("");//根据id修改phone
                }else {
                    Toast.makeText(ModifyPhoneNumberActivity.this,"有错误项",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void phoneOkHttp(String url) {
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder
                .add("id", String.valueOf(userId))
                .add("newPhone",phone.getText().toString())
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功，获取返回数据

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == -9) {
                getTestNumber.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                getTestNumber.setText("获取验证码");
                getTestNumber.setClickable(true);
                i = 30;
            } else if (msg.what == 4) {
                //网络请求失败
                Toast.makeText(ModifyPhoneNumberActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("OK")) {
                    // 操作成功，返回个人界面
                    Toast.makeText(ModifyPhoneNumberActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    finish();
                } else if (msg.obj.equals("")) {
                    Toast.makeText(ModifyPhoneNumberActivity.this, "该手机号已被注册了哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ModifyPhoneNumberActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }else {
                int i = msg.arg1;
                int i1 = msg.arg2;
                Object o = msg.obj;
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    // 短信验证成功后，返回PersonalActivity,然后提示
                } else if (i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    Toast.makeText(ModifyPhoneNumberActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}
