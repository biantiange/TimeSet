package com.example.lt.timeset_andorid.Person;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.lt.timeset_andorid.util.MobUtil.isSpecialChar;

/**
 * SkySong
 */
public class ModifyPasswordActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    private String userPhone;

    private EditText oldPassword;
    private EditText newPassword;
    private EditText surePassword;
    private ImageView oldPasswordImg,newPasswordImg,surePasswordImg;
    private AtomicBoolean isShowPwd = new AtomicBoolean(false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
        //user share phone
        userPhone = share.getString("phone","errorGetPhone");
        findViews();
    }

    private void findViews() {
        //OkHttpClient
        okHttpClient = new OkHttpClient();

        //EditText
        oldPassword = findViewById(R.id.modify_et_password0);
        newPassword = findViewById(R.id.modify_et_password1);
        surePassword = findViewById(R.id.modify_et_password2);
        //ImageView
        oldPasswordImg = findViewById(R.id.modify_iv_pwd0);
        newPasswordImg = findViewById(R.id.modify_iv_pwd1);
        surePasswordImg = findViewById(R.id.modify_iv_pwd2);
        //EditText 内容判断—— when edit text lose focus
        oldPassword.setOnFocusChangeListener((View v,boolean hasFocus)->{
            if (!hasFocus){
                String str = oldPassword.getText().toString();
                SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                String password = share.getString("password","errorGetPassword");
                if (str.equals(password)){
                    oldPasswordImg.setBackgroundResource(R.drawable.right);
                }else{
                    oldPasswordImg.setBackgroundResource(R.drawable.error);
                }
            }
        });
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
                if (isSpecialChar(str)|| str.length()<8 || str.length()>12 ||
                 !surePassword.getText().toString().equals(newPassword.getText().toString()))
                {   //含有非法字符 or different from the previous
                    surePasswordImg.setBackgroundResource(R.drawable.error);
                }else{
                    surePasswordImg.setBackgroundResource(R.drawable.right);
                }
            }
        });
    }


    public void passwordClick(View view) {

        switch (view.getId()){
            case R.id.btn_return_modify_password://返回
                finish();
                break;
            case R.id.password_sure://确认修改
                Drawable.ConstantState drawableCs = getResources().getDrawable(R.drawable.right).getConstantState();
                if (newPasswordImg.getBackground().getConstantState().equals(drawableCs)
                        && surePasswordImg.getBackground().getConstantState().equals(drawableCs)
                        && oldPasswordImg.getBackground().getConstantState().equals(drawableCs)){
                    //过审,->数据库操作
                    passwordOkHttp("");
                }else {
                    Toast.makeText(ModifyPasswordActivity.this,"请检查是否有错误项",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.show_password://显示密码

                if (!isShowPwd.get()){
                    isShowPwd.set(true);
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassword.setSelection(newPassword.getText().toString().length());

                    surePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    surePassword.setSelection(surePassword.getText().toString().length());
                }else {
                    isShowPwd.set(false);
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassword.setSelection(newPassword.getText().toString().length());

                    surePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    surePassword.setSelection(surePassword.getText().toString().length());
                }
                break;
            case R.id.other_way2_password://其他方法（旧密码忘了，要通过手机号修改）
                Intent intent = new Intent(ModifyPasswordActivity.this,ModifyPasswordByPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void passwordOkHttp(String url){

        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder
                .add("password",surePassword.getText().toString())
                .add("phone",userPhone)
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
                Toast.makeText(ModifyPasswordActivity.this,"因网络原因请求失败", Toast.LENGTH_SHORT).show();

            } else if(msg.what== 5){
                //网络请求结果
                if (msg.obj.equals("OK")) {//return String of 'OK'
                    // 操作成功，返回登录界面
                    Toast.makeText(ModifyPasswordActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                    finish();
                } else if (msg.obj.equals("")) {//return String of ''
                    Toast.makeText(ModifyPasswordActivity.this, "该手机号已被注册了哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ModifyPasswordActivity.this, "操作失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
