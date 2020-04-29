package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.lt.timeset_andorid.R;

/**
 * SkySong
 */
public class ModifyPasswordActivity extends AppCompatActivity {
    private EditText oldPassword;
    private EditText newPassword;
    private EditText surePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        findViews();
    }

    private void findViews() {
        //EditText
        oldPassword = findViewById(R.id.modify_et_password0);
        newPassword = findViewById(R.id.modify_et_password1);
        surePassword = findViewById(R.id.modify_et_password2);
    }

    public void passwordClear(View view) {
        switch (view.getId()){
            case R.id.modify_iv_pwd0://旧密码clear

                break;
            case R.id.modify_iv_pwd1://新密码clear

                break;
            case R.id.modify_iv_pwd2://确认密码clear

                break;
        }
    }

    public void passwordClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_password://返回
                finish();
                break;
            case R.id.password_sure://确认修改

                break;
            case R.id.show_password://显示密码

                break;
            case R.id.other_way2_password://其他方法（旧密码忘了，要通过手机号修改）

                break;
        }
    }
}
