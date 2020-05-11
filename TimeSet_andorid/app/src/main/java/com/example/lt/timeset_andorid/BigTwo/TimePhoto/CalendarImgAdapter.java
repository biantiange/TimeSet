package com.example.lt.timeset_andorid.BigTwo.TimePhoto;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewData;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;

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

    private List<String> showImgSource = new ArrayList<>();
    private List<ViewData> vdList;
    private ListView lvOut;
    private ImageViewer iver;
    // 声明列表项中的控件
    public CalendarImgAdapter(Context context, List<Photo> dataSource,
                              int item_layout_id,ListView lvOut,ImageViewer iver) {
        this.context = context;       // 上下文环境
        this.dataSource = dataSource; // 数据源
        this.item_layout_id = item_layout_id; // 列表项布局文件ID
        this.lvOut = lvOut;
        this.iver = iver;
        for (Photo photo : dataSource){
            showImgSource.add(photo.getPath());
        }
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

    // 展示图片
    private void showBigImgs(int position) {
        iver.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iver.setElevation(3.0f);
        }
        vdList = new ArrayList<>();
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

        iver.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    // 正在开启启动预览图片
                    lvOut.setVisibility(View.GONE);
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    lvOut.setVisibility(View.VISIBLE);
                }
            }
        });
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
                showBigImgs(position);
                Log.e("图片点击事件",photo.toString());
            }
        });

        return convertView;
    }
    private  class ViewHolder{
        ImageView img;
    }
}
