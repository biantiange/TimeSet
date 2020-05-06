package com.example.lt.timeset_andorid;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import android.view.View;
import android.view.Window;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;


import com.example.lt.timeset_andorid.Album.AddAlbumActivity;
import com.example.lt.timeset_andorid.Album.GrideAdapter;

import com.example.lt.timeset_andorid.BigTwo.InAlbumActivity;
import com.example.lt.timeset_andorid.Entity.Album;
import com.example.lt.timeset_andorid.Person.PersonalActivity;
import com.example.lt.timeset_andorid.Person.SettingActivity;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * SkySong：抽屉、输入框
 * Sky：主页
 */

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private GridView gridView;
    private GrideAdapter grideAdapter;
    private ImageView img;
    private ImageView addAlbum;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

    private DrawerLayout main_drawer_layout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        initView();
        //设置头像
        View headerView = navigationView.getHeaderView(0);//获取头布局
        ImageView headImg = headerView.findViewById(R.id.person);
        headImg.setImageResource(R.drawable.geren);
        //点击头像划出测边框
        img = findViewById(R.id.img);
        img.setOnClickListener(v -> {
            if(main_drawer_layout.isDrawerOpen(navigationView)){
                main_drawer_layout.closeDrawer(navigationView);
            }else{
                main_drawer_layout.openDrawer(navigationView);
            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item_geren://跳转个人
                    Log.e("MainActivity","ge  ren");
                    Intent intent = new Intent(MainActivity.this,PersonalActivity.class);
                    startActivity(intent);
                    main_drawer_layout.closeDrawer(navigationView);
                    break;
                case R.id.item_setting://跳转设置
                    Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent1);
                    main_drawer_layout.closeDrawer(navigationView);
                    break;
            }
            return true;
        });

        okHttpClient=new OkHttpClient();
        gridView=findViewById(R.id.gride);
        addAlbum=findViewById(R.id.jiahao);
        //头像设置圆形
       /* img=root.findViewById(R.id.img);
        RequestOptions options=new RequestOptions().centerCrop();
        Glide.with(this).load(R.drawable.touxiang).apply(options).into(img);*/
        findDefaultAlbum();
        findAllAlbum();
        grideAdapter=new GrideAdapter(this,list,R.layout.list_gride);
        gridView.setAdapter(grideAdapter);
        addAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlbumActivity.class);
            startActivity(intent);
        });
    }

    private void initView(){
        main_drawer_layout=findViewById(R.id.maindrawer_layout);
        navigationView = findViewById(R.id.nav);
    }

    private void findDefaultAlbum() {
        Log.e("111","111111");
        Request request=new Request.Builder().url(Constant.URL +"album/all?userId=0").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String album= response.body().string();
                Log.e("111",album);
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Album>>(){}.getType();
                List<Album> albumList= gson.fromJson(album,listType);
                inData(albumList);
            }
        });
    }
    private void findAllAlbum() {
        Log.e("111","111111");
        Request request=new Request.Builder().url(Constant.URL +"album/all?userId=1").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String album= response.body().string();
                Log.e("111",album);
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Album>>(){}.getType();
                List<Album> albumList= gson.fromJson(album,listType);
                inData(albumList);
            }
        });
    }

    public void inData(List<Album> albumList){
        Integer [] images={R.drawable.meishi ,R.drawable.jiaoche,R.drawable.fengjing,R.drawable.pengyou };
        for(int i=0;i<albumList.size();i++){
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("image", images[i%4] );
            map1.put("text", albumList.get(i).getAlbumName());
            map1.put("id",albumList.get(i).getId());
            list.add(map1);
        }
    }

}
