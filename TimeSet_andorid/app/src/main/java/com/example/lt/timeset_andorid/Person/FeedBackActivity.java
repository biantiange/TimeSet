package com.example.lt.timeset_andorid.Person;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FeedBackActivity extends AppCompatActivity {
    private ImageView link;
    private Button re;
    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=3366719001";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        link=findViewById(R.id.link);
        re=findViewById(R.id.btn_return_setting);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /****************
                 *
                 * 发起添加群流程。群号：反馈群(518886211) 的 key 为： _8qj0IoNGUdo_ipmcAgl41wtvrzmLeW5
                 * 调用 joinQQGroup(_8qj0IoNGUdo_ipmcAgl41wtvrzmLeW5) 即可发起手Q客户端申请加群 反馈群(518886211)
                 *
                 * @param key 由官网生成的key
                 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
                 ******************/
                String key="_8qj0IoNGUdo_ipmcAgl41wtvrzmLeW5";
               joinQQGroup(key);

            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(this,"未安装手Q或安装的版本不支持",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
