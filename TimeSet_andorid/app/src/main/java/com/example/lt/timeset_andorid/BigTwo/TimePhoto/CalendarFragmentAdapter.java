package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lt.timeset_andorid.R;

import java.util.List;
import java.util.Map;
/**
 * @ClassName
 * @Description 第一层listViewadapter
 * @Author 赵宁
 * @Date 2020-05-03
 */
public class CalendarFragmentAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private List<PhotoList> dataSource; // 声明数据源
    private int item_layout_id; // 声明列表项的布局

    // 声明列表项中的控件
    public CalendarFragmentAdapter(Context context, List<PhotoList> dataSource,
                                   int item_layout_id) {
        this.context = context;       // 上下文环境
        this.dataSource = dataSource; // 数据源
        this.item_layout_id = item_layout_id; // 列表项布局文件ID
    }
    public int getCount() {
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
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(item_layout_id, null);
            holder=new ViewHolder();
            holder.txt_name=convertView.findViewById(R.id.up_time_day);
            holder.gridView=convertView.findViewById(R.id.in_album_list_item_img);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        // 给数据项填充数据



        holder.txt_name.setText(dataSource.get(position).getPtime());
        //gridView填充数据，创建adapter
        List<Photo> phoneList=dataSource.get(position).getPhotoList();
        CalendarImgAdapter calendarImgAdapter=new CalendarImgAdapter(context,phoneList, R.layout.in_album_list_item_gridview_item);
        holder.gridView.setAdapter(calendarImgAdapter);
        //时间

        // 给列表项中的控件注册事件监听器
       /* holder.txt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击更多，显示更多数据
                Toast.makeText(context,"更多数据。。。",Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }
    private  class ViewHolder{
        NoScrollGridView gridView;
        TextView txt_name;
    }
}