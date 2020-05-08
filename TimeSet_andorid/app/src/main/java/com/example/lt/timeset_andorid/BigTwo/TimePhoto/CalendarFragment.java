package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lt.timeset_andorid.Login.LoginActivity;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.utils.Strings;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 赵宁：时间相册
 */
public class CalendarFragment extends Fragment {
    private SharedPreferences sharedPreferences;//获取用户信息
    private List<PhotoList> datasource=new ArrayList<>();
    private ListView listView;
    int albumId;
    int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.in_album_calendar, container, false );
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=getActivity().findViewById(R.id.in_album_list_items);
        //获取相册id值//根据用户id或者用户手机号以及相册id查询相册数据
        albumId=getActivity().getIntent().getIntExtra("albumId",-1);
        sharedPreferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
        userId=sharedPreferences.getInt("id",-1);
        OkHttpClient okHttpClient=new OkHttpClient();
        if(albumId!=-1&&userId!=-1) {
            FormBody.Builder builder = new FormBody.Builder().add("albumId", String.valueOf(albumId)).add("userId", String.valueOf(userId));
            FormBody body = builder.build();
            Request request = new Request.Builder().post(body).url(Constant.URL + "/photo/findByAlbum").build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String strings = response.body().string();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = strings;

                    handler.sendMessage(message);
                }
            });
        }







    }
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           if (msg.what==1){
               Log.e("======","执行失败");
           }
           if(msg.what==2){

               Gson gson=new GsonBuilder().serializeNulls().create();
               List<PhotoList> list= gson.fromJson(msg.obj.toString(),new TypeToken<List<PhotoList>>() {}.getType());
               datasource=list;
               Log.e("s=====================",list.size()+"");
               CalendarFragmentAdapter calendarFragmentAdapter=new CalendarFragmentAdapter(getContext(),datasource, R.layout.in_album_list_item);
               listView.setAdapter(calendarFragmentAdapter);
           }

       }
   };



}
