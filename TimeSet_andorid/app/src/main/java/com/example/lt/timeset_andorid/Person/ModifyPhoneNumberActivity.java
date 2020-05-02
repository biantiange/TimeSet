package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.lt.timeset_andorid.R;

/**
 * SkySong
 */
public class ModifyPhoneNumberActivity extends AppCompatActivity {
    private EditText phone;
    private EditText testNumber;
    private Button getTestNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_number);
        findViews();
    }

    private void findViews() {
        //EditText
        phone = findViewById(R.id.modify_et_phone);
        testNumber = findViewById(R.id.modify_et_yanzhengma);
        //Button
        getTestNumber = findViewById(R.id.modify_btn_yanzhengma);

    }

    public void phoneNumberClick(View view) {
        switch (view.getId()){
            case R.id.btn_return_modify_phone://返回
                finish();
                break;
            case R.id.phoneNum_sure://确认

                break;
            case R.id.modify_iv_phone://clear phone Number

                break;
        }
    }
}
