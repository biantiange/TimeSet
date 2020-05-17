package com.example.lt.timeset_andorid.Search;

import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lt.timeset_andorid.BigTwo.FootEarth.ShowPhotoInfoDialog;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView tvSerach;
    private EditText etSearch;
    private LinearLayout linearLayoutNoSearch;
    private Map<String, List<Photo>> dataSource;
    private SharedPreferences sharedPreferences;
    private static final int GET_LISTVIEW_INF0 = 100;
    private String searchStr = "";
    private ListViewAdapter listViewAdapter;

    private LinearLayout llOut;
    private ImageViewer iver;
    private RelativeLayout rlIverShow;
    private TextView tvPhotoComment;

    private RelativeLayout rlIverEdit;
    private ImageView ivEditExit;
    private TextView tvEditTime;
    private ImageView ivEditDetail;
    private ImageView ivEditDelete;
    private int position;
    private List<String> imgDataSource;
    private List<Photo> photos;
    private InputMethodManager manager;//输入法管理器

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LISTVIEW_INF0:

                    Gson gson = new Gson();
                    dataSource = gson.fromJson(msg.obj.toString(), new TypeToken<Map<String, List<Photo>>>() {
                    }.getType());
                    if (dataSource == null || dataSource.size() == 0) {
                        linearLayoutNoSearch.setVisibility(View.VISIBLE);
                    } else {
                        linearLayoutNoSearch.setVisibility(View.GONE);
                    }
                    listViewAdapter = new ListViewAdapter(searchStr, SearchActivity.this, dataSource, R.layout.activity_search_listview_item);
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
        rlIverShow = findViewById(R.id.rl_search_iver_show_out);
        tvPhotoComment = findViewById(R.id.tv_search_mark_item_comment);
        rlIverEdit = findViewById(R.id.rl_search_iver_show_edit_out);
        ivEditExit = findViewById(R.id.iv_search_iver_show_edit_exit);
        tvEditTime = findViewById(R.id.tv_search_iver_show_edit_time);
        ivEditDetail = findViewById(R.id.iv_search_iver_show_edit_detail);
        ivEditDelete = findViewById(R.id.iv_search_iver_show_edit_delete);
        tvSerach.setOnClickListener(v -> {
            searchStr = etSearch.getText().toString();
            if (searchStr == null) {
                Toast.makeText(SearchActivity.this, "请输入搜索内容！", Toast.LENGTH_SHORT).show();
            }
            Log.e("search", searchStr);
            initListView(searchStr);
        });
        //键盘搜索
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        search();
    }

    private void search() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                //先隐藏键盘
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), 0);
                }
                searchStr = etSearch.getText().toString();
                if (searchStr == null) {
                    Toast.makeText(SearchActivity.this, "请输入搜索内容！", Toast.LENGTH_SHORT).show();
                }
                Log.e("search", searchStr);
                initListView(searchStr);
            }
            //记得返回false
            return false;
        });
    }

    private void initListView(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);
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



    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void viewImage(Map<String, Object> showMap) {
        switch (showMap.get("type").toString()) {
            case "searchshowImg":
                position = (int) showMap.get("position");
                imgDataSource = (List<String>) showMap.get("datasource");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        for (int i = 0, len = imgDataSource.size(); i < len; i++) {
            ViewData viewData = new ViewData();
            viewData.setImageSrc(imgDataSource.get(i));
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
                            dialog.setContext(SearchActivity.this);
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
                        dialog.setContext(SearchActivity.this);
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
                Set<String> keys = dataSource.keySet();
                List<String> list = new ArrayList(keys);
                String str = list.get(0);
                dataSource.get(str).remove(i);
                listViewAdapter.notifyDataSetChanged();
                // 1. 关闭iver
                iver.cancel();
                // 2. 修改数据库
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


}
