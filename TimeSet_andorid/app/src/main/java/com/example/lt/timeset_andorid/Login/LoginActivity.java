package com.example.lt.timeset_andorid.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;

public class LoginActivity extends AppCompatActivity {
//    private OkHttpClient okHttpClient;
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
            getWindow().setStatusBarColor(0xffffcc00);
        }
        findViews();
    }

    private void findViews() {
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
//                        MyOkHttp(Constant.BASE_IP + "LoginServlet?phoneNumber=" + userPhone + "&&password=" + userPwd);
                    }
                    break;
                case R.id.btn_zhuce:
                    //跳转到注册界面
                    Intent intent = new Intent(LoginActivity.this, RegistryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_forgetPwd:
                    Intent intent1 = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }
}
