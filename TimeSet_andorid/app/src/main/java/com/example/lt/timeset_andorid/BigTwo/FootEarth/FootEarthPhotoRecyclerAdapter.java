package com.example.lt.timeset_andorid.BigTwo.FootEarth;


import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;

import java.util.ArrayList;
import java.util.List;

import indi.liyi.viewer.ImageDrawee;
import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;
import indi.liyi.viewer.listener.OnItemChangedListener;
import indi.liyi.viewer.listener.OnItemClickListener;
import indi.liyi.viewer.listener.OnItemLongPressListener;

public class FootEarthPhotoRecyclerAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private List<Photo> dataSource; // 声明数据源
    private int item_layout_id; // 声明列表项的布局
    private RelativeLayout rlOut;
    private ImageViewer iver;
    private RecyclerView rcv;
    private RelativeLayout rlIverShow;
    private TextView tvComment;
    private List<String> showImgSource = new ArrayList<>();

    public FootEarthPhotoRecyclerAdapter(Context context, List<Photo> dataSource, int item_layout_id,
                                         RelativeLayout rlOut, ImageViewer iver,RecyclerView rcv,
                                         RelativeLayout rlIverShow, TextView tvComment) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
        this.rlOut = rlOut;
        this.iver = iver;
        this.rcv = rcv;
        this.rlIverShow = rlIverShow;
        this.tvComment = tvComment;
        for (Photo photo : dataSource) {
            showImgSource.add(Constant.URL + photo.getPath());
//            showImgSource.add(photo.getPath());
        }
    }


    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Photo getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            holder = new ViewHolder();
            holder.rlOut = convertView.findViewById(R.id.rl_foot_earth_item_out);
            holder.ivImg = convertView.findViewById(R.id.iv_foot_earth_item_show);
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            int width = (widthPixels - 30) / 3 ;
            int height = width ;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
            layoutParams.setMargins(2,2,2,2);
            holder.rlOut.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给数据项填充数据
        Photo photo = dataSource.get(position);
        String path = photo.getPath();
        //使用请求选项设置占位符
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.timg)
                .error(R.drawable.timg)
                .fallback(R.drawable.timg);

//        Log.e("paht",Constant.URL+path);
//        Glide.with(context).load(path).apply(requestOptions).into(holder.ivImg);
        Glide.with(context).load(Constant.URL + path).apply(requestOptions).into(holder.ivImg);

        //设置图片单击事件，将phone参数传进图片查看器
        holder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = dataSource.get(position).getPdescribe();
                if (null != comment && !comment.equals(""))
                    tvComment.setText(comment);
                showBigImgs(position);
            }
        });
        return convertView;
    }

    // 展示图片
    private void showBigImgs(int position) {
        iver.setVisibility(View.VISIBLE);
        List<ViewData> vdList = new ArrayList<>();
        Point mScreenSize = ViewDataUtils.getScreenSize(context.getApplicationContext());
        for (int i = 0, len = showImgSource.size(); i < len; i++) {
            ViewData viewData = new ViewData();
            viewData.setImageSrc(showImgSource.get(i));
            viewData.setTargetX(0);
            viewData.setTargetY(0);
            viewData.setTargetWidth(mScreenSize.x);
            viewData.setTargetHeight(ViewDataUtils.dp2px(context.getApplicationContext(), 200));
            vdList.add(viewData);
        }
        iver.overlayStatusBar(false) // ImageViewer 是否会占据 StatusBar 的空间
                .viewData(vdList) // 数据源
                .imageLoader(new PhotoLoader()) // 设置图片加载方式
                .playEnterAnim(true) // 是否开启进场动画，默认为true
                .playExitAnim(true) // 是否开启退场动画，默认为true
                .duration(300) // 设置进退场动画时间，默认300
                .showIndex(false) // 是否显示图片索引，默认为true
//                            .loadIndexUI(indexUI) // 自定义索引样式，内置默认样式
//                            .loadProgressUI(progressUI) // 自定义图片加载进度样式，内置默认样式
                .watch(position);

        // 监听单击事件
        iver.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public boolean onItemClick(int position, ImageView imageView) {
                return true;
            }
        });
        // 监听长点击事件
        iver.setOnItemLongPressListener(new OnItemLongPressListener() {
            @Override
            public boolean onItemLongPress(int position, ImageView imageView) {
                return true;
            }
        });
        // 切换图片
        iver.setOnItemChangedListener(new OnItemChangedListener() {
            @Override
            public void onItemChanged(int position, ImageDrawee drawee) {
                if(rlIverShow.getVisibility() == View.VISIBLE){
                    String comment = dataSource.get(position).getPdescribe();
                    if (null != comment && !comment.equals(""))
                        tvComment.setText(comment);
                }
            }
        });
        iver.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    // 正在开启启动预览图片
                    rlOut.setVisibility(View.GONE);
                    rcv.setVisibility(View.GONE);
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    rlOut.setVisibility(View.VISIBLE);
                    rcv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private class ViewHolder {
        RelativeLayout rlOut;
        ImageView ivImg;
    }
}
