package com.example.lt.timeset_andorid.BigTwo;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 赵宁：点击某个相册后，相册里面的展示
 */
public class InAlbumActivity extends AppCompatActivity {
    private ImageButton return0;
    private TextView btn_search;
    private LinearLayout layout1;
    private LinearLayout layout2;
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
        //获取intent发送的相册id以及相册name
        id11=getIntent().getIntExtra("id",-1);

        setId(id11);
        Log.e("id",getId()+"===================");
        initData();
        findView();
        setListener();
        changeFragment(tabStrId[0]);
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









