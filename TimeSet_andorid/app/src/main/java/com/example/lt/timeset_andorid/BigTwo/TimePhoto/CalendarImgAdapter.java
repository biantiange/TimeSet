package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;

import java.util.Date;
import java.util.List;

/**
 * @ClassName
 * @Description 第二层GridViewAdapter
 * @Author 赵宁
 * @Date 2020-05-03
 */

public class CalendarImgAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private List<Photo> dataSource; // 声明数据源
    private int item_layout_id; // 声明列表项的布局
    // 声明列表项中的控件
    public CalendarImgAdapter(Context context, List<Photo> dataSource,
                              int item_layout_id) {
        this.context = context;       // 上下文环境
        this.dataSource = dataSource; // 数据源
        this.item_layout_id = item_layout_id; // 列表项布局文件ID
    }
    public int getCount() {
        return dataSource.size();
    }
    public Photo getItem(int position) {
        return dataSource.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(item_layout_id, null);
            holder=new ViewHolder();
            holder.img=convertView.findViewById(R.id.grid_view_img);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        // 给数据项填充数据
        Photo photo=dataSource.get(position);
        String path=photo.getPath();
        //使用请求选项设置占位符
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .fallback(R.drawable.timg);
        Log.e("paht",Constant.URL+path);
        Glide.with(context).load(Constant.URL+path).apply(requestOptions).into(holder.img);

        //设置图片单击事件，将phone参数传进图片查看器
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(context,)
                Log.e("图片点击事件",photo.toString());
            }
        });

        return convertView;
    }
    private  class ViewHolder{
        ImageView img;
    }
}
