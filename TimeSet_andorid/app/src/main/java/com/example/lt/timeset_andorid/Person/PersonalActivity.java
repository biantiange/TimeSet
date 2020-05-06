package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.InputTextDialog;

/**
 * SkySong
 */
public class PersonalActivity extends AppCompatActivity {
    private InputTextDialog inputTextDialog;

    private TextView nickName;
    private TextView password;
    private TextView phoneNumber;
    private ImageView headImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        //实例化输入框
        inputTextDialog = new InputTextDialog(PersonalActivity.this,R.style.dialog_center);
        //findViews
        findViews();
    }

    private void findViews() {
        //TextView
        nickName = findViewById(R.id.personal_name);
        password = findViewById(R.id.personal_password);
        phoneNumber = findViewById(R.id.personal_phone);
        //ImageView
        headImg = findViewById(R.id.personal_iv_headImg);

    }

    public void modifyClick(View view) {
        switch (view.getId()){
            case R.id.modify_nickname://修改昵称
                inputTextDialog.setHint("请输入新的昵称");
                inputTextDialog.setMaxNumber(10);//最长十个字
                inputTextDialog.show();
                inputTextDialog.setmOnTextSendListener(msg -> {
                    nickName.setText(msg);
                    //数据库修改

                });

                break;
            case R.id.modify_password://修改密码
                //跳转到修改密码的界面
                Intent intent = new Intent(PersonalActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_phoneNumber://修改手机号
                Intent intent1 = new Intent(PersonalActivity.this,ModifyPhoneNumberActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_return_personal://退出（😂偷懒了，就写这吧）
                finish();
                break;
        }
    }
}
