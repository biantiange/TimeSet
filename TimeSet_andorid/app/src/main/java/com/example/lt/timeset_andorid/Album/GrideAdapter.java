package com.example.lt.timeset_andorid.Album;
/**
 * 王天
 * 照片布局adapter
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.lt.timeset_andorid.R;
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
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
//        Log.e("position",list.get(position));
        holder.textView.setText(list.get(position).get("text").toString());
        Glide.with(context).load(list.get(position).get("image")).into(holder.image);

        //相册的编辑与删除功能
        holder.image.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ImageView shanchu=v.findViewById(R.id.shanchu);
                ImageView bianji=v.findViewById(R.id.bianji);
                okHttpClient=new OkHttpClient();
                if (hasFocus) {
                    Log.e("111","111111");
                    v.findViewById(R.id.album_select).setVisibility(View.VISIBLE);
                   /* shanchu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Request request=new Request.Builder().url(Constant.URL +"album/delete/"+position).build();
                            Call call=okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("删除相册","删除成功");
                                }
                            });
                        }
                    });*/
                } else {
                    Log.e("2222","22222");
                    v.findViewById(R.id.album_select).setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }

    private  class ViewHolder{
        public ImageView image;
        public TextView textView;

    }
}
