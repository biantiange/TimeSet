package com.example.lt.timeset_andorid.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * lz
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Photo> dataSource;
    private int item_layout_id;
    private List<String> showImgSource = new ArrayList<>();
    public GridViewAdapter(Context context, List<Photo> dataSource, int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }

    public int getCount() { return dataSource.size(); }

    public Photo getItem(int position) { return dataSource.get(position); }

    public long getItemId(int position) { return position; }

    public View getView(final int position, View convertView, ViewGroup parent) {
        showImgSource.clear();
        for (Photo photo : dataSource){
            showImgSource.add(Constant.URL+photo.getPath());
        }
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(item_layout_id, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        // 给数据项填充数据
        Photo photo=dataSource.get(position);
        Glide.with(context).load(Constant.URL+photo.getPath()).into(holder.img);

        //设置图片单击事件，将phone参数传进图片查看器
        holder.img.setOnClickListener(v -> {
            //Intent intent=new Intent(context,)
            Map<String,Object> postMap = new HashMap<>();
            postMap.put("type","searchshowImg");
            postMap.put("datasource",showImgSource);
            postMap.put("position",position);
            EventBus.getDefault().post(postMap);
            Log.e("图片点击事件-搜索",photo.toString());
            for (String img:showImgSource){
                Log.e("showImg",img.trim());
            }
        });

        return convertView;
    }
    private class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.gv_img);

        }
    }

}
