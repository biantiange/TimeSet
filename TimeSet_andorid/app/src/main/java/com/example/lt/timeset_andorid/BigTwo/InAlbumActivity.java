package com.example.lt.timeset_andorid.BigTwo;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lt.timeset_andorid.BigTwo.FootEarth.MapFragment;
import com.example.lt.timeset_andorid.BigTwo.FootEarth.ShowPhotoInfoDialog;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.CalendarFragment;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.CalendarFragmentAdapter;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.PhotoList;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.Search.SearchActivity;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import indi.liyi.viewer.ImageDrawee;
import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;
import indi.liyi.viewer.listener.OnItemChangedListener;
import indi.liyi.viewer.listener.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 赵宁：点击某个相册后，相册里面的展示
 */
public class InAlbumActivity extends AppCompatActivity {
    private RelativeLayout rlIverShow;
    private TextView tvPhotoComment;

    private RelativeLayout rlIverEdit;
    private ImageView ivEditExit;
    private TextView tvEditTime;
    private ImageView ivEditDetail;
    private ImageView ivEditDelete;

    private ImageButton return0;
    private TextView btn_time;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout llOut;
    private ImageViewer iver;
    private int albumId;
    private Map<String, MyTabSpec> map = new HashMap<>();
    private String[] tabStrId = {"时间相册", "足迹地球"};
    private Fragment curFragment = null;
    private TextView albumName;//相册名字
    private int id11;//相册id
    // 从EventBus获取的数据源
    private int position;
    private List<String> dataSource;
    private List<Photo> photos;

    private SharedPreferences sharedPreferences;//获取用户信息
    private List<PhotoList> datasource=new ArrayList<>();
    int userId;
    //抽屉
    private DrawerLayout mDrawer;
    private ListView TimeListView;
    private TimeAdapter tAdapter;
    private List<Map<String, Object>> listTime = new ArrayList();

    public int getId() {
        return id11;
    }

    public void setId(int id1) {
        this.id11 = id1;
    }

    private ImageButton addImg;

//    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }

        //获取intent发送的相册id以及相册name
        EventBus.getDefault().register(this);
        id11 = getIntent().getIntExtra("id", -1);
        setId(id11);
//        Log.e("id",getId()+"===================");
        initData();
        findView();
        setListener();
        changeFragment(tabStrId[0]);
        //时间轴
        findTimeData();

//        activity = this;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (iver.onKeyDown(keyCode, event)) {
            return iver.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void viewImage(Map<String, Object> showMap) {
        switch (showMap.get("type").toString()) {
            case "showImg":
                position = (int) showMap.get("position");
                dataSource = (List<String>) showMap.get("datasource");
                photos = (List<Photo>) showMap.get("photoList");
                String comment = photos.get(position).getPdescribe();
                if (null != comment)
                    tvPhotoComment.setText(comment);
                else {
                    tvPhotoComment.setText("");
                }
                rlIverShow.setVisibility(View.VISIBLE);
                iver.setBackgroundColor(Color.parseColor("#000000"));
                showBigImgs();
                break;
        }
    }

    // 展示图片
    private void showBigImgs() {
        iver.setVisibility(View.VISIBLE);
        llOut.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iver.setElevation(3.0f);
        }
        List<ViewData> vdList = new ArrayList<>();
        Point mScreenSize = ViewDataUtils.getScreenSize(this);
        for (int i = 0, len = dataSource.size(); i < len; i++) {
            ViewData viewData = new ViewData();
            viewData.setImageSrc(dataSource.get(i));
            viewData.setTargetX(0);
            viewData.setTargetY(0);
            viewData.setTargetWidth(mScreenSize.x);
            viewData.setTargetHeight(ViewDataUtils.dp2px(this, 200));
            vdList.add(viewData);
        }
        iver.overlayStatusBar(true) // ImageViewer 是否会占据 StatusBar 的空间
                .viewData(vdList) // 数据源
                .imageLoader(new PhotoLoader()) // 设置图片加载方式
                .playEnterAnim(true) // 是否开启进场动画，默认为true
                .playExitAnim(true) // 是否开启退场动画，默认为true
                .duration(300) // 设置进退场动画时间，默认300
                .showIndex(false) // 是否显示图片索引，默认为true
//                            .loadIndexUI(indexUI) // 自定义索引样式，内置默认样式
//                            .loadProgressUI(progressUI) // 自定义图片加载进度样式，内置默认样式
                .watch(position);

        // 监听单击事件
        iver.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public boolean onItemClick(final int i, ImageView imageView) { // 将展示修改界面
                if (rlIverShow.getVisibility() == View.VISIBLE) {

                    rlIverShow.setVisibility(View.GONE);    // 展示界面消失
                    rlIverEdit.setVisibility(View.VISIBLE);
                    iver.setBackgroundColor(Color.parseColor("#ffffff"));  // 背景色变白
                    // 展示修改界面显示
                    ivEditExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iver.cancel();
                        }
                    });
                    String time = photos.get(i).getPtime();
                    if (null != time) {
                        tvEditTime.setText(time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日");
                    } else {
                        tvEditTime.setText("");
                    }
                    ivEditDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowPhotoInfoDialog dialog = ShowPhotoInfoDialog.getDialog();
                            dialog.setContext(InAlbumActivity.this);
                            dialog.setPhoto(photos.get(i));
                            dialog.showBottomDialog();
                        }
                    });
                    ivEditDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteDialog(i, photos.get(i));
                        }
                    });
                } else if (rlIverEdit.getVisibility() == View.VISIBLE) {  // 将展示展示界面
                    rlIverEdit.setVisibility(View.GONE);
                    rlIverShow.setVisibility(View.VISIBLE); // 展示界面显示
                    iver.setBackgroundColor(Color.parseColor("#000000"));// 背景色变黑
                    // 修改comment
                    String comment = photos.get(i).getPdescribe();
                    if (null != comment)
                        tvPhotoComment.setText(comment);
                    else {
                        tvPhotoComment.setText("");
                    }
                }
                return true;    // 返回值为true，不会退出展示
            }
        });
        // 切换图片
        iver.setOnItemChangedListener(new OnItemChangedListener() {
            @Override
            public void onItemChanged(int i, ImageDrawee drawee) {
                ivEditDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPhotoInfoDialog dialog = ShowPhotoInfoDialog.getDialog();
                        dialog.setContext(InAlbumActivity.this);
                        dialog.setPhoto(photos.get(i));
                        dialog.showBottomDialog();
                    }
                });
                if (rlIverShow.getVisibility() == View.VISIBLE) {
                    String comment = photos.get(i).getPdescribe();
                    if (null != comment)
                        tvPhotoComment.setText(comment);
                    else {
                        tvPhotoComment.setText("");
                    }
                }
            }
        });
        iver.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    // 正在开启启动预览图片
                    llOut.setVisibility(View.GONE);
                    iver.setVisibility(View.VISIBLE);
                    rlIverShow.setVisibility(View.VISIBLE);
                    rlIverEdit.setVisibility(View.GONE);    // 进来时展示展示界面
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    llOut.setVisibility(View.VISIBLE);
                    iver.setVisibility(View.GONE);
                    rlIverShow.setVisibility(View.GONE);
                    rlIverEdit.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showDeleteDialog(int i, Photo photo) {
        // 显示一个Dialog
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setTitle("确定删除此图片");

        adBuilder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 1. 删除数据源
                Map<String, String> deleteMap = new HashMap<>();
                deleteMap.put("type", "delete");
                deleteMap.put("position", i + "");
                deleteMap.put("time", photo.getPtime());
                EventBus.getDefault().post(deleteMap);
                // 2. 关闭iver
                iver.cancel();
                // 3. 修改数据库
                changeDelete(photo.getId());
            }
        });
        adBuilder.setNegativeButton("我手滑了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 选中“取消”按钮，取消界面
            }
        });
        adBuilder.create().show();
    }

    private static final String IP = Constant.IP + "/photo/delete";

    private void changeDelete(int id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder().add("photoId", id + "");
        FormBody body = builder.build();
        Request request = new Request.Builder().post(body).url(IP).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Toast.makeText(context,"delete失败",Toast.LENGTH_SHORT).show();
                Log.e("deleteee", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String strings = response.body().string();
//                Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                Log.e("deletee", "成果");
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

    //时间轴
    private void findView() {
        layout1 = findViewById(R.id.tab_spec_1);
        layout2 = findViewById(R.id.tab_spec_2);
        addImg = findViewById(R.id.add_img);
        return0 = findViewById(R.id.btn_return1);
        btn_time = findViewById(R.id.btn_time);
        albumName = findViewById(R.id.album_name);
        llOut = findViewById(R.id.ll_in_album_out);
        iver = findViewById(R.id.iver_show_img);
        albumName.setText(getIntent().getStringExtra("albumName"));
        //抽屉
        mDrawer = findViewById(R.id.drawer_layout);
        TimeListView = findViewById(R.id.list_time);
        // 展示
        rlIverShow = findViewById(R.id.rl_in_album_iver_show_out);
        tvPhotoComment = findViewById(R.id.tv_in_album_mark_item_comment);
        rlIverEdit = findViewById(R.id.rl_in_album_iver_show_edit_out);
        ivEditExit = findViewById(R.id.iv_in_album_iver_show_edit_exit);
        tvEditTime = findViewById(R.id.tv_in_album_iver_show_edit_time);
        ivEditDetail = findViewById(R.id.iv_in_album_iver_show_edit_detail);
        ivEditDelete = findViewById(R.id.iv_in_album_iver_show_edit_delete);

    }

    private void setListener() {
        MyListener listener = new MyListener();
        layout1.setOnClickListener(listener);
        layout2.setOnClickListener(listener);
        return0.setOnClickListener(listener);
        btn_time.setOnClickListener(listener);
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
        if (curFragment == fragment) return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        if (curFragment != null)
            transaction.hide(curFragment);
        if (!fragment.isAdded()) {
            transaction.add(R.id.tab_content, fragment);
        }
        transaction.show(fragment);
        transaction.commit();
        curFragment = fragment;
    }

    private class MyListener implements View.OnClickListener {
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
                    albumId = getIntent().getIntExtra("albumId", -1);
                    Intent intent = new Intent(InAlbumActivity.this, AddPictureActivity.class).putExtra("albumId", albumId);
                    startActivity(intent);

                    break;

                case R.id.btn_return1:
                    finish();
                    break;
                case R.id.btn_time:
                    if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
                        mDrawer.openDrawer(GravityCompat.START);
                    }
                    break;
            }
        }
    }

    private void findTimeData(){
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        userId=sharedPreferences.getInt("id",-1);
        albumId = getIntent().getIntExtra("albumId", -1);
        OkHttpClient okHttpClient=new OkHttpClient();
        if(albumId!=-1&&userId!=-1) {
            FormBody.Builder builder = new FormBody.Builder().add("albumId",  String.valueOf(albumId)).add("userId", String.valueOf(userId));
            FormBody body = builder.build();
            Request request = new Request.Builder().post(body).url(Constant.URL + "/photo/findByAlbum").build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String strings = response.body().string();
                    Log.e("time111111111",strings);
                    Message message=new Message();
                    message.what=1;
                    message.obj=strings;
                    handler.sendMessage(message);

                }
            });
        }
    }

    private void findTime(List<PhotoList> datasource) {
        Log.e("asdfghj","sdfghjk");
        HashSet<String> hashSet = new HashSet<>();
        for (int j = 0; j < datasource.size(); j++) {
            hashSet.add(datasource.get(j).getPtime().substring(0, 6));
        }
        TreeSet<String> treeSet = new TreeSet<>(hashSet);
        treeSet.comparator();

        for (String str : treeSet) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("time", str);
            map.put("statu", 0);
            listTime.add(map);
        }
            tAdapter = new TimeAdapter(this, listTime);
            TimeListView.setAdapter(tAdapter);
            TimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    EventBus.getDefault().post(listTime.get(position).get("time"));
                    listTime.get(position).put("statu", 1);
                    tAdapter.notifyDataSetChanged();
                    mDrawer.closeDrawers();
                }
            });
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<PhotoList> list= gson.fromJson(msg.obj.toString(),new TypeToken<List<PhotoList>>() {}.getType());
                datasource = list;
                findTime(datasource);
            }

        }
    };
}









