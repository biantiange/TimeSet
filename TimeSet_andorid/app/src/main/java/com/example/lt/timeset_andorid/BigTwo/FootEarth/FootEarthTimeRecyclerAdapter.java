package com.example.lt.timeset_andorid.BigTwo.FootEarth;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lt.timeset_andorid.BigTwo.TimePhoto.CalendarImgAdapter;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.NoScrollGridView;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.Photo;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.PhotoList;
import com.example.lt.timeset_andorid.R;

import java.util.List;

import indi.liyi.viewer.ImageViewer;

public class FootEarthTimeRecyclerAdapter extends RecyclerView.Adapter {
    private List<PhotoList> photoLists;
    private Context context;
    private int layout_item_id;
    private RelativeLayout rlOut;
    private ImageViewer iver;
    private RelativeLayout rlIverShow;
    private TextView tvComment;
    private RecyclerView rcv;
    public FootEarthTimeRecyclerAdapter(List<PhotoList> photoLists, Context context, int layout_item_id,
                                        RelativeLayout rlOut, ImageViewer iver,RecyclerView rcv,
                                        RelativeLayout rlIverShow, TextView tvComment) {
        this.photoLists = photoLists;
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.rlOut = rlOut;
        this.iver = iver;
        this.rcv = rcv;
        this.rlIverShow = rlIverShow;
        this.tvComment = tvComment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        String time = photoLists.get(position).getPtime();
        Log.e("Timeeeeee",time);
        myViewHolder.tvTime.setText(time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日");
        //gridView填充数据，创建adapter
        List<Photo> phoneList = photoLists.get(position).getPhotoList();
        FootEarthPhotoRecyclerAdapter adapter = new FootEarthPhotoRecyclerAdapter(context, phoneList, R.layout.item_foot_earth_images, rlOut,iver,rcv,rlIverShow,tvComment);
        myViewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if (photoLists == null){
            return 0;
        }else{
            return photoLists.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        NoScrollGridView gridView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_foot_earth_out_time);
            gridView = itemView.findViewById(R.id.in_album_list_item_img);
        }
    }
}
