package com.example.lt.timeset_andorid.BigTwo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lt.timeset_andorid.R;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaData;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.lt.timeset_andorid.BigTwo.AddPictureActivity.GET_IMAGES_FROM_ADD;

public class AddPictureRecyclerAdapter extends RecyclerView.Adapter {
    private List<LocalMedia> res ;
    private Context context;
    private int layout_item_id;
    private boolean deleteShow = false;
    public AddPictureRecyclerAdapter(List<LocalMedia> res, Context context, int layout_item_id) {
        this.res = res;
        this.context = context;
        this.layout_item_id = layout_item_id;
        res.add(new LocalMedia());  // 添加一个可以添加图片的位置
    }

    public void updateRes(List<LocalMedia> res){
        this.res = res;
        res.add(new LocalMedia());  // 添加一个可以添加图片的位置
        this.deleteShow = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, parent, false);

        // 获取屏幕宽度
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int width = (widthPixels - 15)/3;
        int height = width;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
        view.setLayoutParams(layoutParams);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position < res.size()-1){
            LocalMedia media = res.get(position);
            Glide.with(context).load(media.getPath()).into(viewHolder.ivImg);
            if (deleteShow == false){
                viewHolder.ivDelete.setVisibility(View.GONE);
            }else{
                viewHolder.ivDelete.setVisibility(View.VISIBLE);
            }
            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 从res中删除此项
                    res.remove(position);
                    notifyDataSetChanged();
                }
            });
            viewHolder.rlOut.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteShow = true;
                    notifyDataSetChanged();
                    return true;
                }
            });
        }else if (position == res.size() -1){
            Glide.with(context).load(R.drawable.ic_add_image).into(viewHolder.ivImg);
            viewHolder.ivDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == res){
            return 0;
        }else {
            return res.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlOut;
        ImageView ivImg;
        ImageView ivDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOut = itemView.findViewById(R.id.rl_add_picture_item_out);
            ivImg = itemView.findViewById(R.id.iv_add_picture_item_img);
            ivDelete = itemView.findViewById(R.id.iv_add_picture_item_delete);
        }
    }
}
