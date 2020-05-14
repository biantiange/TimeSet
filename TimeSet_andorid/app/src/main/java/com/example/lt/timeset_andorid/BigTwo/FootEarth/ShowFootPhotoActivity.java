package com.example.lt.timeset_andorid.BigTwo.FootEarth;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lt.timeset_andorid.BigTwo.TimePhoto.Photo;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.PhotoList;
import com.example.lt.timeset_andorid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import indi.liyi.viewer.ImageViewer;

public class ShowFootPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageViewer iver;
    private RecyclerView recyShow;
    private RelativeLayout rlOut;
    private ImageView ivExit;
    private TextView tvCount;
    private TextView tvCity;
    private RelativeLayout rlIverShow;
    private TextView tvPhotoComment;
    private FootEarthTimeRecyclerAdapter adapter;

    private String city = "北京";
    private int total = 0;
    private List<PhotoList> photos;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_foot_photo);

        initDatas();
        findViews();
        initViews();
        setListeners();
        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new FootEarthTimeRecyclerAdapter(photos, this, R.layout.item_foot_earth_time, rlOut, iver, recyShow,rlIverShow,tvPhotoComment);
        recyShow.setAdapter(adapter);
    }

    private void setListeners() {
        ivExit.setOnClickListener(this);
    }

    private void initViews() {
        for (PhotoList pl : photos){
            total += pl.getPhotoList().size();
        }
        tvCount.setText("" + total);
        tvCity.setText(""+city);
    }

    private void findViews() {
        iver = findViewById(R.id.iver_foot_earth_show);
        rlOut = findViewById(R.id.rl_show_foot_earth_out);
        recyShow = findViewById(R.id.rcv_foot_earth_images);
        ivExit = findViewById(R.id.iv_foot_earth_show_exit);
        tvCount = findViewById(R.id.tv_foot_earth_show_img_count);
        tvCity = findViewById(R.id.tv_foot_earth_show_img_province);
        rlIverShow = findViewById(R.id.rl_foot_earth_iver_show_out);
        tvPhotoComment = findViewById(R.id.tv_foot_earth_mark_item_comment);
    }

    private void initDatas() {
        String photoStr = getIntent().getStringExtra("photos");
        city =getIntent().getStringExtra("city");
        Type type = new TypeToken<List<PhotoList>>() {}.getType();
        photos = gson.fromJson(photoStr,type);

//        // 1. 获取照片数据( 模拟通过intent获取phonoList数据源)
//        photos = new ArrayList<>();
//        Photo photo = new Photo();
//        photo.setPlace("北京");
//        photo.setPtime("20200202");
//        photo.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//        Photo photo1 = new Photo();
//        Photo photo4 = new Photo();
//        photo4.setPlace("北京");
//        photo4.setPtime("20200202");
//        photo4.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//        Photo photo5 = new Photo();
//        photo5.setPlace("北京");
//        photo5.setPtime("20200202");
//        photo5.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//
//        Photo photo6 = new Photo();
//        photo6.setPlace("北京");
//        photo6.setPtime("20200202");
//        photo6.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
//
//        photo1.setPlace("北京");
//        photo1.setPtime("20200202");
//        photo1.setPath("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
//        Photo photo2 = new Photo();
//        photo2.setPlace("北京");
//        photo2.setPtime("20200203");
//        photo2.setPath("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3252521864,872614242&fm=26&gp=0.jpg");
//        Photo photo3 = new Photo();
//        photo3.setPlace("北京");
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
//        photos.add(photoList1);
//        photos.add(photoList2);
//        photos.add(photoList3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_foot_earth_show_exit:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (iver.onKeyDown(keyCode, event)) {
            return iver.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
