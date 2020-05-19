package com.example.lt.timeset_andorid.Person;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class PrivacyAgreementActivity extends AppCompatActivity {
    private TextView sc;
    private Button re;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_agreement);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        sc=findViewById(R.id.sc);
        re=findViewById(R.id.btn_return_setting);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
