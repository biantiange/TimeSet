package com.example.lt.timeset_andorid.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.lt.timeset_andorid.BigTwo.TimePhoto.NoScrollGridView;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * lz
 */
public class ListViewAdapter extends BaseAdapter {
    private String searchStr;
    private Context context;
    private Map<String, List<Photo>> dataSource;
    private int item_layout_id;

    // 声明列表项中的控件
    public ListViewAdapter(String searchStr,Context context, Map<String, List<Photo>> dataSource, int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
        this.searchStr=searchStr;
    }

    public int getCount() {
        if(dataSource==null){
            return 0;
        }
        return dataSource.size();
    }

    public Object getItem(int position) {
        return dataSource.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给数据项填充数据
        Set keys = dataSource.keySet();
        List list = new ArrayList(keys);
        String str = (String) list.get(position);
        if(str.equals("place")){
            holder.title.setText("拍摄地点:"+searchStr);
        }
        if(str.equals("identify")){
            holder.title.setText("图片内容包含"+searchStr);
        }
        if(str.equals("contain")){
            holder.title.setText("描述信息包含"+searchStr);
        }
        //gridView填充数据，创建adapter
        List<Photo> photos = dataSource.get(str);
        GridViewAdapter gridviewAdapter = new GridViewAdapter(context, photos, R.layout.activity_search_gridview_item);
        holder.gridView.setAdapter(gridviewAdapter);

        return convertView;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public GridView gridView;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gv_photo);
            title = itemView.findViewById(R.id.search_title);

        }
    }
}
