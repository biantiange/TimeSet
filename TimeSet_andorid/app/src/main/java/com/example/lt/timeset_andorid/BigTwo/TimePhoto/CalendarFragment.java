package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

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

import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 赵宁：时间相册
 */
public class CalendarFragment extends Fragment {
    private SharedPreferences sharedPreferences;//获取用户信息
    private List<PhotoList> datasource=new ArrayList<>();
    private CalendarFragmentAdapter calendarFragmentAdapter;
    private ListView listView;
    int albumId;
    int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.in_album_calendar, container, false );
        EventBus.getDefault().register(this);
        return newView;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteItem(Map<String,String> deleteMap){
        if (deleteMap.get("type").equals("delete")){
            int position = Integer.parseInt(deleteMap.get("position"));
            String time = deleteMap.get("time");
            for (int i=0;i<datasource.size();i++){
                PhotoList pl = datasource.get(i);
                if (pl.getPtime().equals(time)){
                    pl.getPhotoList().remove(position);
                    if (pl.getPhotoList().size() == 0){
                        datasource.remove(pl);
                    }
                    break;
                }
            }
            calendarFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=getActivity().findViewById(R.id.in_album_list_items);
        //获取相册id值//根据用户id或者用户手机号以及相册id查询相册数据
        albumId=getActivity().getIntent().getIntExtra("albumId",-1);
        sharedPreferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
        userId=sharedPreferences.getInt("id",-1);

        OkHttpClient okHttpClient = new OkHttpClient();
        if (albumId != -1 && userId != -1) {
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
               Log.e("s=====================",list.size()+""+list.toString());
               calendarFragmentAdapter=new CalendarFragmentAdapter(getContext(),datasource, R.layout.in_album_list_item);
               listView.setAdapter(calendarFragmentAdapter);
           }

       }
   };

//    private void getData() {
//        // 1. 获取照片数据( 模拟通过intent获取phonoList数据源)
//        datasource = new ArrayList<>();
//        Photo photo = new Photo();
//        photo.setPlace("北京");
//        photo.setPtime("20200202");
//        photo.setPdescribe("asdsadf");
//        photo.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//        Photo photo1 = new Photo();
//        Photo photo4 = new Photo();
//        photo4.setPlace("北京");
//        photo4.setPtime("20200202");
//        photo4.setPdescribe("asdsggggadf");
//        photo4.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//        Photo photo5 = new Photo();
//        photo5.setPlace("北京");
//        photo5.setPtime("20200202");
//        photo5.setPdescribe("asdsggggadf123");
//        photo5.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//
//        Photo photo6 = new Photo();
//        photo6.setPlace("北京");
//        photo6.setPtime("20200202");
//        photo6.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//
//        photo1.setPlace("上海");
//        photo1.setPtime("20200202");
//        photo1.setPath("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
//        Photo photo2 = new Photo();
//        photo2.setPlace("广西");
//        photo2.setPtime("20200203");
//        photo2.setPath("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3252521864,872614242&fm=26&gp=0.jpg");
//        Photo photo3 = new Photo();
//        photo3.setPlace("成都");
//        photo3.setPtime("20200204");
//        photo3.setPath("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3252521864,872614242&fm=26&gp=0.jpg");
//        List<Photo> photoList = new ArrayList<>();
//        photoList.add(photo);
//        photoList.add(photo1);
//        photoList.add(photo4);
//        photoList.add(photo5);
//        photoList.add(photo6);
//        List<Photo> photos1 = new ArrayList<>();
//        photos1.add(photo2);
//        List<Photo> photos2 = new ArrayList<>();
//        photos2.add(photo3);
//        PhotoList photoList1 = new PhotoList();
//        photoList1.setPhotoList(photoList);
//        photoList1.setPtime("20200202");
//        PhotoList photoList2 = new PhotoList();
//        photoList2.setPhotoList(photos1);
//        photoList2.setPtime("20200203");
//        PhotoList photoList3 = new PhotoList();
//        photoList3.setPhotoList(photos2);
//        photoList3.setPtime("20200204");
//        datasource.add(photoList1);
//        datasource.add(photoList2);
//        datasource.add(photoList3);
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
