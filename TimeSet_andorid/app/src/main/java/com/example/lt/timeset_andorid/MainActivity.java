package com.example.lt.timeset_andorid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lt.timeset_andorid.Album.AddAlbum;
import com.example.lt.timeset_andorid.Album.Album;
import com.example.lt.timeset_andorid.Album.GrideAdapter;
import com.example.lt.timeset_andorid.Login.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private GridView gridView;
    private GrideAdapter grideAdapter;
    private ImageView img;
    private ImageView addAlbum;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    private ActionBarDrawerToggle drawerbar;
    private DrawerLayout main_drawer_layout;
    private LinearLayout main_left_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_drawer_layout.isDrawerOpen(main_left_layout)){
                    main_drawer_layout.closeDrawer(main_left_layout);
                }else{
                    main_drawer_layout.openDrawer(main_left_layout);
                }
            }
        });
        okHttpClient=new OkHttpClient();
        gridView=findViewById(R.id.gride);
        addAlbum=findViewById(R.id.jiahao);
        //头像设置圆形
       /* img=root.findViewById(R.id.img);
        RequestOptions options=new RequestOptions().centerCrop();
        Glide.with(this).load(R.drawable.touxiang).apply(options).into(img);*/
        //相册的排列
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("image", R.drawable.meishi );
        map1.put("text", "美食");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("image", R.drawable.jiaoche);
        map2.put("text", "兴趣");
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("image", R.drawable.fengjing);
        map3.put("text", "风景");
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("image", R.drawable.pengyou);
        map4.put("text", "朋友");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        //  findAllAlbum();
        grideAdapter=new GrideAdapter(this,list,R.layout.list_gride);
        gridView.setAdapter(grideAdapter);

        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlbum.class);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        main_drawer_layout=findViewById(R.id.maindrawer_layout);
        main_drawer_layout.setScrimColor(Color.TRANSPARENT);
        main_left_layout=findViewById(R.id.main_left_drawer_layout);
    }
    private void initEvent(){
        drawerbar=new ActionBarDrawerToggle(this,main_drawer_layout,R.string.open,R.string.close);
        main_drawer_layout.setDrawerListener(drawerbar);
    }

    private void findAllAlbum() {
        Request request=new Request.Builder().url(Constant.URL +"album/all").build();
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
                if(!album.equals("")){
                    Gson gson=new Gson();
                    Type listType=new TypeToken<List<Album>>(){}.getType();
                    List<Album> albumList= gson.fromJson(album,listType);
                    inData(albumList);
                }
            }
        });
    }

    public void inData(List<Album> albumList){
        for(int i=0;i<albumList.size();i++){
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("image", R.drawable.touxiang);
            map1.put("text", albumList.get(i).getAlbumName());
            list.add(map1);
        }
    }

}
