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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;


import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.MobUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.lt.timeset_andorid.util.MobUtil.APPKEY;
import static com.example.lt.timeset_andorid.util.MobUtil.APPSECRET;
import static com.example.lt.timeset_andorid.util.MobUtil.isSpecialChar;


/**
 * SkySong
 */
public class ModifyPasswordByPhoneActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    private String phone;

    private EditText newPassword,surePassword;

    private EditText testNumber;
    private Button getTestNumber;

    private ImageView newPasswordImg,surePasswordImg;
    private int i = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password_by_phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        SharedPreferences share  = getSharedPreferences("user",MODE_PRIVATE);
        phone = share.getString("phone","errorGetPhone");

        findViews();
        // 2.1  先注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.setAskPermisionOnReadContact(true);
        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);

        //2.2注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void findViews() {
        //OkHttpClient
        okHttpClient = new OkHttpClient();
        //EditText
        newPassword = findViewById(R.id.modify_et_password2phone);
        surePassword = findViewById(R.id.modify_et_password2phone2);
        testNumber = findViewById(R.id.modify_et_yanzhengma2phone);
        //Button
        getTestNumber = findViewById(R.id.modify_btn_yanzhengma2phone);
        //ImageView
        newPasswordImg = findViewById(R.id.modify_iv_pwd2phone);
        surePasswordImg = findViewById(R.id.modify_iv_pwd2phone2);
        //EditText 内容判断—— when edit text lose focus
        newPassword.setOnFocusChangeListener((View v,boolean hasFocus)->{
            if (!hasFocus){
                String str = newPassword.getText().toString();
                if (isSpecialChar(str)|| str.length()<8 || str.length()>12) {   //含有非法字符
                    newPasswordImg.setBackgroundResource(R.drawable.error);
                }else{
                    newPasswordImg.setBackgroundResource(R.drawable.right);
                }
            }
        });
        surePassword.setOnFocusChangeListener((View v,boolean hasFocus) -> {
            if (!hasFocus){
                String str = surePassword.getText().toString();
                if (isSpecialChar(str)|| str.length()<8 || str.length()>12)
                {   //含有非法字符 or different from the previous
                    surePasswordImg.setBackgroundResource(R.drawable.error);
                }else{
                    surePasswordImg.setBackgroundResource(R.drawable.right);
                }
            }
        });
    }


    public void phonePasswordClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_password2phone:// come back
                finish();
                break;
            case R.id.password_sure2phone:// take sure
                Log.e("提交", "点击了");
                if (testNumber.getText() == null || testNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(ModifyPasswordByPhoneActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phone, testNumber.getText().toString());
                break;

            case R.id.modify_btn_yanzhengma2phone:// 点击获取验证码
                String phoneNum = phone;
                // 1. 判断手机号是不是11位并且看格式是否合理
                getCheckCode(phoneNum);
                break;

        }
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

    //点击提交后，验证码正确之后的判断方法
    public void submit_after() {
        Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
        //将收到的验证码和手机号提交再次核对
        SMSSDK.submitVerificationCode("86", phone, testNumber.getText().toString());
        if (newPasswordImg.getBackground().getConstantState().equals(drawableCs)){
            if (surePassword.getText().toString().equals(newPassword.getText().toString())){
                //通过验证
                passwordOkHttp(Constant.IP + "user/updateUserPassword?phone=" + phone + "&&password="+surePassword.getText().toString());
            }else {
                Toast.makeText(ModifyPasswordByPhoneActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(ModifyPasswordByPhoneActivity.this,"检查错误项",Toast.LENGTH_SHORT).show();
        }
    }

    //获取验证码
    public void getCheckCode(String phoneNum) {
        if (!MobUtil.judgePhoneNums(phoneNum)) {
            Toast.makeText(ModifyPasswordByPhoneActivity.this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ModifyPasswordByPhoneActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("0")) {
                    // 操作成功，返回个人界面
                    Toast.makeText(ModifyPasswordByPhoneActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor= share.edit();
                    editor.putString("password",surePassword.getText().toString());
                    editor.commit();
                    finish();
                } else if (msg.obj.equals("-1")){
                    Toast.makeText(ModifyPasswordByPhoneActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }else {
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
//                        Toast.makeText(ModifyPasswordByPhoneActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO 处理错误的结果
                        Log.e("进入验证-获取",""+o);
                        ((Throwable) o).printStackTrace();
                    }
                } else if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (i1 == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        //验证码通过后在进行忘记密码操作
//                        Toast.makeText(ModifyPasswordByPhoneActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(PhoneTestActivity.this, "提交验证码成功,可以进行后续操作了", Toast.LENGTH_SHORT).show();
                        //提交其他判断的
                        submit_after();
                    } else {
                        // TODO 处理错误的结果
                        Log.e("进入验证-提交",""+o);
                        Throwable t = (Throwable) o;
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        map1 = new Gson().fromJson(t.getMessage(),new TypeToken<HashMap<String,Object>>(){}.getType());
//                        Toast.makeText(ModifyPasswordByPhoneActivity.this,"错误码:"+map1.get("status")+"\n错误信息："+map1.get("description"),Toast.LENGTH_LONG).show();
                        Log.e("提交验证码错误信息",t.getMessage());
                        ((Throwable) o).printStackTrace();
                    }
                }
            }
        }
    };

    private void passwordOkHttp(String url){

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
}
