package com.example.lt.timeset_andorid.BigTwo;


import android.content.Intent;

import android.graphics.Point;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lt.timeset_andorid.BigTwo.FootEarth.MapFragment;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.CalendarFragment;
import com.example.lt.timeset_andorid.MainActivity;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.Search.SearchActivity;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;

/**
 * 赵宁：点击某个相册后，相册里面的展示
 */
public class InAlbumActivity extends AppCompatActivity {
    private ImageButton return0;
    private TextView btn_search;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout llOut;
    private ImageViewer iver;
    private int albumId;
    private Map<String, MyTabSpec> map = new HashMap<>();
    private String [] tabStrId = {"时间相册", "足迹地球"};
    private Fragment curFragment = null;
    private TextView albumName;//相册名字
    private int id11;//相册id
    public int getId(){return id11;}
    public void setId(int id1){this.id11=id1;}
    private ImageButton addImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }

        //获取intent发送的相册id以及相册name
        EventBus.getDefault().register(this);
        id11=getIntent().getIntExtra("id",-1);

        setId(id11);
        Log.e("id",getId()+"===================");
        initData();
        findView();
        setListener();
        changeFragment(tabStrId[0]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (iver.onKeyDown(keyCode, event)) {
            return iver.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void viewImage(Map<String,Object> showMap){
        switch (showMap.get("type").toString()){
            case "showImg":

                int position = (int) showMap.get("position");
                List<String> dataSource = (List<String>) showMap.get("datasource");
                Log.e("Subscribe",dataSource.get(position));
                showBigImgs(position,dataSource);
                break;
        }
    }
    // 展示图片
    private void showBigImgs(int position,List<String> showImgSource) {
        iver.setVisibility(View.VISIBLE);
        llOut.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iver.setElevation(3.0f);
        }
        List<ViewData> vdList = new ArrayList<>();
        Point mScreenSize = ViewDataUtils.getScreenSize(getApplicationContext());
        for (int i = 0, len = showImgSource.size(); i < len; i++) {
            ViewData viewData = new ViewData();
            viewData.setImageSrc(showImgSource.get(i));
            viewData.setTargetX(0);
            viewData.setTargetY(0);
            viewData.setTargetWidth(mScreenSize.x);
            viewData.setTargetHeight(ViewDataUtils.dp2px(getApplicationContext(), 200));
            vdList.add(viewData);
        }
        for (ViewData data:vdList){
            Log.e("vdList",""+data.getImageSrc().toString());
        }
        iver.overlayStatusBar(false) // ImageViewer 是否会占据 StatusBar 的空间
                .viewData(vdList) // 数据源
                .imageLoader(new PhotoLoader()) // 设置图片加载方式
                .playEnterAnim(true) // 是否开启进场动画，默认为true
                .playExitAnim(true) // 是否开启退场动画，默认为true
                .duration(300) // 设置进退场动画时间，默认300
                .showIndex(false) // 是否显示图片索引，默认为true
//                            .loadIndexUI(indexUI) // 自定义索引样式，内置默认样式
//                            .loadProgressUI(progressUI) // 自定义图片加载进度样式，内置默认样式
                .watch(position);

        iver.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    // 正在开启启动预览图片
                    llOut.setVisibility(View.GONE);
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    llOut.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        map.put(tabStrId[0], new MyTabSpec());
        map.put(tabStrId[1], new MyTabSpec());
        map.get(tabStrId[0]).setFragment(new CalendarFragment());
        map.get(tabStrId[1]).setFragment(new MapFragment());
    }
    private void findView() {
        layout1 = findViewById(R.id.tab_spec_1);
        layout2 = findViewById(R.id.tab_spec_2);
        addImg=findViewById(R.id.add_img);
        return0=findViewById(R.id.btn_return1);
        btn_search=findViewById(R.id.btn_search);
        albumName=findViewById(R.id.album_name);
        llOut = findViewById(R.id.ll_in_album_out);
        iver = findViewById(R.id.iver_show_img);
        albumName.setText(getIntent().getStringExtra("albumName"));
    }
    private void setListener() {
        MyListener listener = new MyListener();
        layout1.setOnClickListener(listener);
        layout2.setOnClickListener(listener);
        return0.setOnClickListener(listener);
        btn_search.setOnClickListener(listener);
        addImg.setOnClickListener(listener);
    }
    private class MyTabSpec {
        private Fragment fragment = null;
        public Fragment getFragment() {
            return fragment;
        }
        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }
    private void changeFragment(String s) {
        Fragment fragment = map.get(s).getFragment();
        if(curFragment == fragment) return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        if(curFragment!=null)
            transaction.remove(curFragment);
        if(!fragment.isAdded()) {
            transaction.add(R.id.tab_content, fragment);
        }
        transaction.show(fragment);
        curFragment = fragment;
        transaction.commit();
    }
    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tab_spec_1:
                    changeFragment(tabStrId[0]);

                    break;
                case R.id.tab_spec_2:
                    changeFragment(tabStrId[1]);
                    break;
                case R.id.add_img:
                    albumId=getIntent().getIntExtra("albumId",-1);
                   Intent intent=new Intent(InAlbumActivity.this,AddPictureActivity.class ).putExtra("albumId",albumId);
                   startActivity(intent);
                   finish();
                   break;

                case R.id.btn_return1:
                    finish();
                    break;
                case R.id.btn_search:
                    Intent intent1=new Intent(InAlbumActivity.this, SearchActivity.class);
                    startActivity(intent1);

                    break;
            }
        }
    }
}









