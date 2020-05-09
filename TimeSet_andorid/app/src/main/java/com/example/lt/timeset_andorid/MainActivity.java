package com.example.lt.timeset_andorid;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import android.view.View;
import android.view.Window;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.Album.AddAlbumActivity;
import com.example.lt.timeset_andorid.Album.GrideAdapter;

import com.example.lt.timeset_andorid.BigTwo.AddPictureActivity;
import com.example.lt.timeset_andorid.BigTwo.InAlbumActivity;
import com.example.lt.timeset_andorid.Entity.Album;
import com.example.lt.timeset_andorid.Entity.User;
import com.example.lt.timeset_andorid.Person.PersonalActivity;
import com.example.lt.timeset_andorid.Person.SettingActivity;
import com.example.lt.timeset_andorid.Search.SearchActivity;
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
    private ImageView search;
    private ImageView addAlbum;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    int userId;
    SharedPreferences sharedPreferences;

    private DrawerLayout main_drawer_layout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  SDKInitializer.initialize(getApplicationContext());
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }*/
        sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        userId=sharedPreferences.getInt("id",1);
        //个人设置
        initView();
        //设置头像and nickName（抽屉内的）
        View headerView = navigationView.getHeaderView(0);//获取头布局
        ImageView headImg = headerView.findViewById(R.id.person);
        String headImgPath= sharedPreferences.getString( "headImg", "" );
        RequestOptions options=new RequestOptions().circleCrop().placeholder( R.drawable.touxiang).error( R.drawable.touxiang );
        Glide.with(this).load(headImgPath).apply(options).into(headImg);
        TextView nickName = headerView.findViewById(R.id.nickName);
        nickName.setText(sharedPreferences.getString("userName","errorGetName"));
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
        //主界面
        okHttpClient=new OkHttpClient();
        gridView=findViewById(R.id.gride);
        addAlbum=findViewById(R.id.jiahao);
        img=findViewById(R.id.img);
        search=findViewById(R.id.sousuo);
        findUserPic();
        findDefaultAlbum();
        findAllAlbum();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        addAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlbumActivity.class);
            startActivity(intent);
        });
    }
    private void findUserPic() {
        String headImgPath= sharedPreferences.getString( "headImg", "" );
        RequestOptions options=new RequestOptions().circleCrop().placeholder( R.drawable.touxiang).error( R.drawable.touxiang );
        Glide.with(this).load(headImgPath).apply(options).into(img);
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
                Message message=new Message();
                message.what=1;
                message.obj=album;
                handler.sendMessage(message);

            }
        });
    }
    private void findAllAlbum() {
        Log.e("111","111111");
        Request request=new Request.Builder().url(Constant.URL +"album/all?userId="+userId).build();
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
                if(album.equals("")){
                   Log.e("无相册","无相册");
                }
                else{
                    Message message=new Message();
                    message.what=2;
                    message.obj=album;
                    handler.sendMessage(message);
                }
            }
        });
    }
    public void inData(List<Album> albumList){
        Integer [] images={R.drawable.meishi ,R.drawable.jiaoche,R.drawable.fengjing,R.drawable.pengyou };
        for(int i=0;i<albumList.size();i++){
            Map<String, Object> map1 = new HashMap<String, Object>();
           /* map1.put("image",images[i%4]);*/
            map1.put("text", albumList.get(i).getAlbumName());
            map1.put("id",albumList.get(i).getId());
            switch (albumList.get(i).getTheme()){
                case "美食":
                    map1.put("image", R.drawable.meimei);
                    break;
                case "兴趣":
                    map1.put("image", R.drawable.huahau);
                    break;
                case "风景":
                    map1.put("image", R.drawable.fengjingjing );
                    break;
                case "朋友":
                    map1.put("image", R.drawable.pengyou );
                    break;
                case "普通":
                    map1.put("image",R.drawable.putong);
                    break;
                case "亲子":
                    map1.put("image",R.drawable.qinzi );
                    break;
                case "情侣":
                    map1.put("image", R.drawable.qinglv );
                    break;
                case "家庭":
                    map1.put("image", R.drawable.jiating );
                    break;
                case "旅游":
                    map1.put("image", R.drawable.lvyouu);
                    break;
           }
            list.add(map1);
        }
        grideAdapter=new GrideAdapter(MainActivity.this,list,R.layout.list_gride);
        gridView.setAdapter(grideAdapter);
    }
    private void initView(){
        main_drawer_layout=findViewById(R.id.maindrawer_layout);
        navigationView = findViewById(R.id.nav);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Album>>(){}.getType();
                List<Album> albumList= gson.fromJson((String) msg.obj,listType);
                inData(albumList);
            }
            if(msg.what==2){
                Gson gson=new Gson();
                Type listType=new TypeToken<List<Album>>(){}.getType();
                List<Album> albumList= gson.fromJson((String) msg.obj,listType);
                inData(albumList);
            }
        }
    };

}
