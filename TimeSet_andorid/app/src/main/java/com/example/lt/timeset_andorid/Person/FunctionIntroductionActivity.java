package com.example.lt.timeset_andorid.Person;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lt.timeset_andorid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class FunctionIntroductionActivity extends AppCompatActivity {
    private Button re;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_introduction);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }

        re=findViewById(R.id.btn_return_setting);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
