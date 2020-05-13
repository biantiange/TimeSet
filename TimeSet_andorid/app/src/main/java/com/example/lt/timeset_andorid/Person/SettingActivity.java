package com.example.lt.timeset_andorid.Person;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.Login.LoginActivity;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.CacheClear;

import java.io.File;

/**
 * SkySong
 */
public class SettingActivity extends AppCompatActivity {
    private final int UPDATE_TEXT = 326520;
    private String string;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        findViews();
        initData();
    }

    private void findViews() {
        textView = findViewById(R.id.clean_number);
    }

    public void settingClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return_setting://返回
                finish();
                break;
            case R.id.clean_cache:
                onClickCleanCache();
                break;
            case R.id.esc://退出登录
                //清空SharedPreference
                SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                share.edit().clear().commit();
                //跳转到登录页面
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 弹出是否清理的对话框
     */
    private void onClickCleanCache() {
        getConfirmDialog(this, "是否清空缓存?", (dialogInterface, i) -> {
            CacheClear.cleanInternalCache(getApplicationContext());
            Message msg = new Message();
            msg.what = UPDATE_TEXT;
            msg.obj = "0.0Byte";
            handle.sendMessage(msg);
            Toast.makeText(SettingActivity.this, "清除完毕", Toast.LENGTH_SHORT).show();
        }).show();
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }
    /**
     * TODO：计算当前的缓存大小
     */
    private String getDataSize() {
        long fileSize = 0;
        File filesDir = getFilesDir();
        File cacheDir = getCacheDir();
        fileSize += CacheClear.getDirSize(filesDir);
        fileSize += CacheClear.getDirSize(cacheDir);
        String formatSize = CacheClear.getFormatSize(fileSize);
        return formatSize;
    }
    /**
     * TODO：获取当前缓存所占的空间大小
     */
    private void initData() {
        string = getDataSize();
        textView.setText(string.equals("0.0Byte") ? "" : string);
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    string = (String) msg.obj;
                    textView.setText(string.equals("0.0Byte") ? "" : string);
                    break;
                default:
                    break;
            }
        }
    };
}
