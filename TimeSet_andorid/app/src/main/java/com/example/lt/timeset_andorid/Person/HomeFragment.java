package com.example.lt.timeset_andorid.Person;
/**
 * 王天
 * 首页
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lt.timeset_andorid.Album.AddAlbum;
import com.example.lt.timeset_andorid.Entity.Album;
import com.example.lt.timeset_andorid.Album.GrideAdapter;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private OkHttpClient okHttpClient;
    private GridView gridView;
    private GrideAdapter grideAdapter;
    private ImageView img;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        okHttpClient=new OkHttpClient();
        gridView=root.findViewById(R.id.gride);
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
        grideAdapter=new GrideAdapter(getContext(),list,R.layout.list_gride);
        gridView.setAdapter(grideAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),AddAlbum.class);
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.album_select).setVisibility(View.VISIBLE);
                return false;
            }
        });
        return root;
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
