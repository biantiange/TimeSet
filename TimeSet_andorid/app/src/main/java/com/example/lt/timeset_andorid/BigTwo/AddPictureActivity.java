package com.example.lt.timeset_andorid.BigTwo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lt.timeset_andorid.Album.GrideAdapter;
import com.example.lt.timeset_andorid.Login.LoginActivity;
import com.example.lt.timeset_andorid.Login.RegistryActivity;
import com.example.lt.timeset_andorid.R;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

public class AddPictureActivity extends AppCompatActivity {
    private ImageButton fanhui;
    private TextView upLoad;
    private EditText text;
    private GridView gridView;
    private static final int REQUEAT_SELECT_CODE = 100;
    private static final int REQUEST_CAMERA_CODE = 120;
    private static final int CAMERA_OK = 140;
    private static final int READ_OK = 160;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album_add_img);
        findView();
        setAdapter();
    }


    private void findView() {
        gridView=findViewById(R.id.add_img_gridView);
        fanhui=findViewById(R.id.btn_return11);
        upLoad=findViewById(R.id.add_img_in_album);
        text=findViewById(R.id.bei_zhu);
    }
    private void setAdapter() {
        MyListener myListener=new MyListener();
        fanhui.setOnClickListener(myListener);
        upLoad.setOnClickListener(myListener);
    }


    private class MyListener implements View

            .OnClickListener{
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btn_return11:
                    //返回
                    finish();
                case R.id.add_img_in_album:
                    //添加照片
                    /**
                     * 获取图片
                     */
                    if (ContextCompat.checkSelfPermission(AddPictureActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AddPictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AddPictureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddPictureActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA}, READ_OK);
                    } else {
                        ISListConfig config = new ISListConfig.Builder()
                                // 是否多选, 默认true
                                .multiSelect(false)
                                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                                .rememberSelected(true)
                                // “确定”按钮背景色
                                .btnBgColor(Color.GRAY)
                                // “确定”按钮文字颜色
                                .btnTextColor(Color.BLUE)
                                // 使用沉浸式状态栏
                                .statusBarColor(Color.parseColor("#3F51B5"))
                                // 返回图标ResId
                                .backResId(R.drawable.abc_action_bar_item_background_material)
                                // 标题
                                .title("选择图片")
                                // 标题文字颜色
                                .titleColor(Color.WHITE)
                                // TitleBar背景色
                                .titleBgColor(Color.parseColor("#3F51B5"))
                                // 裁剪大小。needCrop为true的时候配置
                                .cropSize(1, 1, 200, 200)
                                .needCrop(true)
                                // 第一个是否显示相机，默认true
                                .needCamera(false)
                                // 最大选择图片数量，默认9
                                .maxNum(1)
                                .build();
                        // 跳转到图片选择器
                        ISNav.getInstance().toListActivity(this, config, REQUEAT_SELECT_CODE);
                    }

                }
            }

        }
    }
