package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lt.timeset_andorid.R;

/**
 * SkySong
 */
public class ModifyPasswordByPhoneActivity extends AppCompatActivity {
    private EditText newPassword,surePassword;
    private EditText phone;
    private EditText testNumber;
    private Button getTestNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password_by_phone);
        findViews();
    }

    private void findViews() {
        //EditText
        newPassword = findViewById(R.id.modify_et_password2phone);
        surePassword = findViewById(R.id.modify_et_password2phone2);
        phone = findViewById(R.id.modify_et_phone2phone);
        testNumber = findViewById(R.id.modify_et_yanzhengma2phone);
        //Button
        getTestNumber = findViewById(R.id.modify_btn_yanzhengma2phone);
    }

    public void passwordPhoneClear(View view) {
        switch (view.getId()){
            case R.id.modify_iv_pwd2phone://clear new password

                break;
            case R.id.modify_iv_pwd2phone2:// clear surePassword

                break;
            case R.id.modify_iv_phone2phone://clear phone number

                break;
        }
    }

    public void phonePasswordClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_password2phone:// come back
                finish();
                break;
            case R.id.password_sure2phone:// take sure

                break;
        }
    }
}
