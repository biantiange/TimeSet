package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.lt.timeset_andorid.Login.LoginActivity;
import com.example.lt.timeset_andorid.R;

/**
 * SkySong
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    public void settingClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_setting://返回
                finish();
                break;
            case R.id.esc://退出登录
                //清空SharedPreference
                SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                share.edit().clear().commit();
                //跳转到登录页面
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
