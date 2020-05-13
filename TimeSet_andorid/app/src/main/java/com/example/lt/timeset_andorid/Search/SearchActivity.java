package com.example.lt.timeset_andorid.Search;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
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
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView tvSerach;
    private EditText etSearch;
    private LinearLayout linearLayoutNoSearch;
    private Map<String, List<Photo>> dataSource;
    private SharedPreferences sharedPreferences;
    private static final int GET_LISTVIEW_INF0 = 100;
    private String searchStr = "";
    private LinearLayout llOut;
    private ImageViewer iver;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LISTVIEW_INF0:

                    Gson gson = new Gson();
                    dataSource = gson.fromJson(msg.obj.toString(), new TypeToken<Map<String, List<Photo>>>() {
                    }.getType());
                    if (dataSource == null ||  dataSource.size() == 0) {
                        linearLayoutNoSearch.setVisibility(View.VISIBLE);
                    } else {
                        linearLayoutNoSearch.setVisibility(View.GONE);
                    }
                    ListViewAdapter listViewAdapter = new ListViewAdapter(searchStr, SearchActivity.this, dataSource, R.layout.activity_search_listview_item);
                    listView.setAdapter(listViewAdapter);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        //获取
        EventBus.getDefault().register(this);
        etSearch = findViewById(R.id.et_search);
        linearLayoutNoSearch = findViewById(R.id.ll_no_search);
        listView = findViewById(R.id.lv_search);
        tvSerach = findViewById(R.id.tv_search);
        llOut = findViewById(R.id.ll_search_in_album_out);
        iver = findViewById(R.id.iver_search_show_img);
        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStr = etSearch.getText().toString();
                if(searchStr == null){
                    Toast.makeText(SearchActivity.this,"请输入搜索内容！",Toast.LENGTH_SHORT).show();
                }
                Log.e("search", searchStr);
                initListView(searchStr);
            }
        });
    }

    private void initListView(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        sharedPreferences=this.getSharedPreferences("user",MODE_PRIVATE);
        int userId=sharedPreferences.getInt("id",-1);
        FormBody.Builder builder = new FormBody.Builder().add("str", str + "").add("userId", String.valueOf(userId));
        FormBody body = builder.build();
        Request request = new Request.Builder().post(body).url(Constant.URL + "photo/find").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("search", "f");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("search", json);
                Message message = new Message();
                message.what = GET_LISTVIEW_INF0;
                message.obj = json;
                handler.sendMessage(message);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void viewImage(Map<String, Object> showMap) {
        switch (showMap.get("type").toString()) {
            case "searchshowImg":
                int position = (int) showMap.get("position");
                List<String> dataSource = (List<String>) showMap.get("datasource");
                Log.e("Subscribe_show",dataSource.get(position));
                showBigImgs(position, dataSource);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    // 展示图片
    private void showBigImgs(int position, List<String> showImgSource) {
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
        for (ViewData data : vdList) {
//            Log.e("vdList",""+data.getImageSrc().toString());
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


}
