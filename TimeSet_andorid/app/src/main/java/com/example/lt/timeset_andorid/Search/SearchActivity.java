package com.example.lt.timeset_andorid.Search;

import android.content.SharedPreferences;
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

import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LISTVIEW_INF0:

                    Gson gson = new Gson();
                    dataSource = gson.fromJson(msg.obj.toString(), new TypeToken<Map<String, List<Photo>>>() {
                    }.getType());
                    if (dataSource.size() == 0) {
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
        etSearch = findViewById(R.id.et_search);
        linearLayoutNoSearch = findViewById(R.id.ll_no_search);
        listView = findViewById(R.id.lv_search);
        tvSerach = findViewById(R.id.tv_search);
        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStr = etSearch.getText().toString();
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


}
