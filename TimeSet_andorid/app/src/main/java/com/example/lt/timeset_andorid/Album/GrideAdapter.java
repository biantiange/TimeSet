package com.example.lt.timeset_andorid.Album;
/**
 * sky
 * 照片adapter
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.lt.timeset_andorid.BigTwo.InAlbumActivity;
import com.example.lt.timeset_andorid.MainActivity;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GrideAdapter extends BaseAdapter {
    private List<Map<String,Object>> list=new ArrayList<>();
    private  int itemResId;
    private Context context;
    private OkHttpClient okHttpClient;

    public GrideAdapter(Context context,List<Map<String,Object>> list, int itemResId) {
        this.list = list;
        this.itemResId = itemResId;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(itemResId,null);
            holder=new ViewHolder();
            holder.image=convertView.findViewById(R.id.image);
            holder.textView=convertView.findViewById(R.id.text);
            holder.shanchu=convertView.findViewById(R.id.shanchu);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).get("text").toString());
        Glide.with(context).load(list.get(position).get("image") ).into(holder.image);

        //跳转相片
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InAlbumActivity.class);
                context.startActivity(intent);
            }
        });
        //相册的编辑与删除功能
        final  EditText edtext =convertView.findViewById(R.id.text);
        holder.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("得到焦点","的叫");
                } else {
                   Log.e("视角",edtext.getText().toString());
                   edtext.setText(edtext.getText());
                }
            }
        });
          holder.shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okHttpClient=new OkHttpClient();
                int id=(Integer) list.get(position).get("id");
                Log.e("删除id",id+"");
                Request request=new Request.Builder().url(Constant.URL +"album/delete?id="+id).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private  class  ViewHolder{
        public ImageView image;
        public ImageView shanchu;
        public EditText textView;
    }
}
