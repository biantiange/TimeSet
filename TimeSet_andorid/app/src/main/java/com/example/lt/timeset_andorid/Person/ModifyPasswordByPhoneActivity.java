package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lt.timeset_andorid.R;

import static com.example.lt.timeset_andorid.util.MobUtil.isSpecialChar;

/**
 * SkySong
 */
public class ModifyPasswordByPhoneActivity extends AppCompatActivity {
    private EditText newPassword,surePassword;
    private EditText testNumber;
    private Button getTestNumber;
    private ImageView newPasswordImg,surePasswordImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password_by_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        findViews();
    }

    private void findViews() {
        //EditText
        newPassword = findViewById(R.id.modify_et_password2phone);
        surePassword = findViewById(R.id.modify_et_password2phone2);
        testNumber = findViewById(R.id.modify_et_yanzhengma2phone);
        //Button
        getTestNumber = findViewById(R.id.modify_btn_yanzhengma2phone);
        //ImageView
        newPasswordImg = findViewById(R.id.modify_iv_pwd2phone);
        surePasswordImg = findViewById(R.id.modify_iv_pwd2phone2);
        //when edit text changed
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

    public void phonePasswordClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_password2phone:// come back
                finish();
                break;
            case R.id.password_sure2phone:// take sure

                break;
            case R.id.modify_btn_yanzhengma2phone:// 点击获取验证码

                break;
        }
    }
}
