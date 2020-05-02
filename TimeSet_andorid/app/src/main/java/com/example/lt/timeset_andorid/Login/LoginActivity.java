package com.example.lt.timeset_andorid.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.lt.timeset_andorid.Entity.User;
import com.example.lt.timeset_andorid.util.Constant;

import com.example.lt.timeset_andorid.MainActivity;
import com.example.lt.timeset_andorid.R;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 卢朋娇
 */


public class LoginActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    //控件
    private EditText etUserPhone;
    private EditText etUserPwd;
    private Button btnLook;
    private Button btnLogin;
    private Button btnRegist;
    private Button btnForget;

    //控件值
    private String userPhone;
    private String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        findViews();

        // 2. 判断sharedP中是否已经有登录用户
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if (sharedPreferences!=null && sharedPreferences.getBoolean("flag", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void findViews() {
        okHttpClient = new OkHttpClient();
        //控件
        etUserPhone = findViewById(R.id.et_userPhone);
        etUserPwd = findViewById(R.id.et_userPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnRegist = findViewById(R.id.btn_zhuce);  //注册
        btnForget = findViewById(R.id.btn_forgetPwd);

        //给按钮设置监听器
        MyListener myListener = new MyListener();
        btnLogin.setOnClickListener(myListener);
        btnRegist.setOnClickListener(myListener);
        btnForget.setOnClickListener(myListener);

    }
    public class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    userPhone = etUserPhone.getText().toString();
                    userPwd = etUserPwd.getText().toString();
                    Log.e("LoginActivity", "手机号：" + userPhone + "密码：" + userPwd);
                    if (userPhone == null || userPhone.equals("") || userPwd == null || userPwd.equals("")) {
                        Toast.makeText(LoginActivity.this, "请按要求输入哦", Toast.LENGTH_SHORT).show();
                    } else {
                        //发给数据库做验证
                        MyOkHttp(Constant.IP + "user/login?phone=" + userPhone + "&&password=" + userPwd);
                    }
                    break;
                case R.id.btn_zhuce:
                    Intent intent = new Intent(LoginActivity.this,RegistryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_forgetPwd:
                    Intent intent1 = new Intent(LoginActivity.this, PhoneTestActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

    public void MyOkHttp(String url) {
        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //登录失败
                handler.sendEmptyMessage(1);   //网络原因请求失败
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //登录成功，返回的是user的全部信息
                String jsonStr = response.body().string();
                Log.e("LoginActivity", "响应：" + jsonStr);
                //登录成功，返回全部信息
                if (jsonStr != null && !jsonStr.equals("")) {
                    Message message=new Message();
                    message.what=2;
                    message.obj=jsonStr;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Toast.makeText(LoginActivity.this,"因网络原因请求失败",Toast.LENGTH_SHORT).show();
            }else if(msg.what == 3){
                Toast.makeText(LoginActivity.this,"用户名或密码错误，登录失败",Toast.LENGTH_SHORT).show();
            }else{   // ==2登录成功
                //存到SharedPreferences中并显示登录成功，跳转到MainActivity
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
               // 保存到SharedPreferences
                save(msg.obj.toString());
                //跳转到MainActivity界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void save(String jsonStr){
        Log.e("LoginActivity", "要开始存储了");
        User user = new Gson().fromJson(jsonStr,User.class);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", user.getPhone());
        editor.putString("headImg", user.getHeadImg());
        editor.putString("userName", user.getUserName());
        editor.putString("password",user.getPassword());
        editor.putBoolean("flag" , true);   //是否第一次登录，如果不是则后续登录不用输密码
        editor.commit();   //提交
        Log.e("LoginActivity", "存储结束了" + user.toString() );
    }
}
