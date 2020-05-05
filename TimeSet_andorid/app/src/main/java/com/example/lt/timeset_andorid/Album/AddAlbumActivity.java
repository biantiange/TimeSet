package com.example.lt.timeset_andorid.Album;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lt.timeset_andorid.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 赵宁w
 */
public class AddAlbumActivity extends AppCompatActivity {
    private String type="普通";//相册类型
    private ImageButton return0;
    private Button toadd;
    private EditText album_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_album);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8 );
        }

        initData();
        setListener();
        changeTab(tabStrId[0]);
    }
    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.tab_spec_1:
                    type="普通";
                    changeTab(tabStrId[0]);
                    break;
                case R.id.tab_spec_2:
                    type="朋友";
                    changeTab(tabStrId[1]);
                    break;
                case R.id.tab_spec_3:
                    type="亲子";
                    changeTab(tabStrId[2]);
                    break;
                case R.id.tab_spec_4:
                    type="旅游";
                    changeTab(tabStrId[3]);
                    break;
                case R.id.tab_spec_5:
                    type="情侣";
                    changeTab(tabStrId[4]);
                    break;
                case R.id.tab_spec_6:
                    type="家庭";
                    changeTab(tabStrId[5]);
                    break;
                case R.id.to_add_album:
                    //完成创建,获取名字，类型
                    String name=album_name.getText().toString();
                    int userId=1;//从shapernece获取
                    if(name.isEmpty()){
                        Toast.makeText(AddAlbumActivity.this,"相册名称不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        //与服务器交互，将name，type，userId传入服务器
                        OkHttpClient client=new OkHttpClient();
                        FormBody.Builder re=new FormBody.Builder();
                        RequestBody requestBody=re.add("userId", String.valueOf(userId))
                                .add("name",name)
                                .add("type",type).build();
                        Request request=new Request.Builder().url("").post(requestBody).build();//ip参数

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(AddAlbumActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                            }

                            @SuppressLint("WrongConstant")
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string=response.body().string();//添加成功返回1，否则返回0
                                if(string.equals("1")){
                                    //跳转页面到图片选择器
                                    finish();
                                }
                                else{
                                    Toast.makeText(AddAlbumActivity.this,"请检查网络111",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }
                    break;
                case R.id.btn_return0:
                    finish();
                    break;
            }
        }
    }

    private void changeTab(String s) {
        changeImage(s);
    }


    private void changeImage(String s) {
        for (String key : map.keySet()) {
            map.get(key).setSelect(false);
        }
        map.get(s).setSelect(true);
    }

    private void setListener() {
        LinearLayout layout1 = findViewById(R.id.tab_spec_1);
        LinearLayout layout2 = findViewById(R.id.tab_spec_2);
        LinearLayout layout3 = findViewById(R.id.tab_spec_3);
        LinearLayout layout4= findViewById(R.id.tab_spec_4);
        LinearLayout layout5= findViewById(R.id.tab_spec_5);
        LinearLayout layout6= findViewById(R.id.tab_spec_6);

        MyListener listener = new MyListener();
        layout1.setOnClickListener(listener);
        layout2.setOnClickListener(listener);
        layout3.setOnClickListener(listener);
        layout4.setOnClickListener(listener);
        layout5.setOnClickListener(listener);
        layout6.setOnClickListener(listener);
        return0.setOnClickListener(listener);
        toadd.setOnClickListener(listener);

    }

    private void initData() {
        map.put(tabStrId[0], new MyTabSpec());
        map.put(tabStrId[1], new MyTabSpec());
        map.put(tabStrId[2], new MyTabSpec());
        map.put(tabStrId[3], new MyTabSpec());
        map.put(tabStrId[4], new MyTabSpec());
        map.put(tabStrId[5], new MyTabSpec());

        findView();
        setImage();
    }

    private void setImage() {
        map.get(tabStrId[0]).setNormalImage(R.drawable.ordinary);
        map.get(tabStrId[0]).setSelectImage(R.drawable.ordinary1);
        map.get(tabStrId[1]).setNormalImage(R.drawable.friend);
        map.get(tabStrId[1]).setSelectImage(R.drawable.friend1);
        map.get(tabStrId[2]).setNormalImage(R.drawable.parent_child);
        map.get(tabStrId[2]).setSelectImage(R.drawable.parent_child1);
        map.get(tabStrId[3]).setNormalImage(R.drawable.travel);
        map.get(tabStrId[3]).setSelectImage(R.drawable.travel1);
        map.get(tabStrId[4]).setNormalImage(R.drawable.lovers);
        map.get(tabStrId[4]).setSelectImage(R.drawable.lovers1);
        map.get(tabStrId[5]).setNormalImage(R.drawable.home);
        map.get(tabStrId[5]).setSelectImage(R.drawable.home1);


    }


    private void findView() {
        return0=findViewById(R.id.btn_return0);
        toadd=findViewById(R.id.to_add_album);
        album_name=findViewById(R.id.album_name);

        ImageView iv1 = findViewById(R.id.img_1);
        ImageView iv2 = findViewById(R.id.img_2);
        ImageView iv3 = findViewById(R.id.img_3);
        ImageView iv4 = findViewById(R.id.img_4);
        ImageView iv5 = findViewById(R.id.img_5);
        ImageView iv6 = findViewById(R.id.img_6);

        TextView tv1 = findViewById(R.id.tv_1);
        TextView tv2 = findViewById(R.id.tv_2);
        TextView tv3 = findViewById(R.id.tv_3);
        TextView tv4= findViewById(R.id.tv_4);
        TextView tv5 = findViewById(R.id.tv_5);
        TextView tv6= findViewById(R.id.tv_6);

        map.get(tabStrId[0]).setImageView(iv1);
        map.get(tabStrId[0]).setTextView(tv1);

        map.get(tabStrId[1]).setImageView(iv2);
        map.get(tabStrId[1]).setTextView(tv2);

        map.get(tabStrId[2]).setImageView(iv3);
        map.get(tabStrId[2]).setTextView(tv3);


        map.get(tabStrId[3]).setImageView(iv4);
        map.get(tabStrId[3]).setTextView(tv4);
        map.get(tabStrId[4]).setImageView(iv5);
        map.get(tabStrId[4]).setTextView(tv5);
        map.get(tabStrId[5]).setImageView(iv6);
        map.get(tabStrId[5]).setTextView(tv6);

    }


    private class MyTabSpec {
        private ImageView imageView = null;
        private TextView textView = null;
        private int normalImage;
        private int selectImage;



        private void setSelect(boolean b) {
            if(b) {
                imageView.setImageResource(selectImage);
                textView.setTextColor(
                        Color.parseColor("#1E90FF"));
            } else {
                imageView.setImageResource(normalImage);
                textView.setTextColor(
                        Color.parseColor("#000000"));
            }
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }


        public void setTextView(TextView textView) {
            this.textView = textView;
        }


        public void setNormalImage(int normalImage) {
            this.normalImage = normalImage;
        }

        public void setSelectImage(int selectImage) {
            this.selectImage = selectImage;
        }


    }

    private Map<String, MyTabSpec> map = new HashMap<>();
    private String [] tabStrId = {"普通", "朋友", "亲子","旅游","情侣","家庭"};


}
