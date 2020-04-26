package com.example.lt.timeset_andorid.Album;
/**
 * 王天
 * 照片布局adapter
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lt.timeset_andorid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrideAdapter extends BaseAdapter {
    private List<Map<String,Object>> list=new ArrayList<>();
    private  int itemResId;
    private Context context;

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


        return convertView;
    }

    private  class ViewHolder{
        public ImageView image;
        public TextView textView;
    }
}
