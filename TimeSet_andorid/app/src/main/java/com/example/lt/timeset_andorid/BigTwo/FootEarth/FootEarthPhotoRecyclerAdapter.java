package com.example.lt.timeset_andorid.BigTwo.FootEarth;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.example.lt.timeset_andorid.util.PhotoLoader;
import com.example.lt.timeset_andorid.util.ViewDataUtils;

import java.io.IOException;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FootEarthPhotoRecyclerAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private List<Photo> dataSource; // 声明数据源
    private int item_layout_id; // 声明列表项的布局
    private RelativeLayout rlOut;
    private ImageViewer iver;
    private RecyclerView rcv;
    private RelativeLayout rlIverShow;
    private TextView tvComment;
    private RelativeLayout rlIverEdit;
    private ImageView ivEditExit;
    private TextView tvEditTime;
    private ImageView ivEditDetail;
    private ImageView ivEditDelete;
    private TextView tvCount;
    private List<String> showImgSource = new ArrayList<>();

    public FootEarthPhotoRecyclerAdapter(Context context, List<Photo> dataSource, int item_layout_id,
                                         RelativeLayout rlOut, ImageViewer iver, RecyclerView rcv,TextView tvCount,
                                         RelativeLayout rlIverShow, TextView tvComment,
                                         RelativeLayout rlIverEdit, ImageView ivEditExit, TextView tvEditTime, ImageView ivEditDetail, ImageView ivEditDelete) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
        this.rlOut = rlOut;
        this.iver = iver;
        this.rcv = rcv;
        this.tvCount = tvCount;
        this.rlIverShow = rlIverShow;
        this.tvComment = tvComment;
        this.rlIverEdit = rlIverEdit;
        this.ivEditExit = ivEditExit;
        this.tvEditTime = tvEditTime;
        this.ivEditDetail = ivEditDetail;
        this.ivEditDelete = ivEditDelete;
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
            int width = (widthPixels - 30) / 3;
            int height = width;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.setMargins(2, 2, 2, 2);
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
                rlIverShow.setVisibility(View.VISIBLE);
                iver.setBackgroundColor(Color.parseColor("#000000"));
                showBigImgs(position);
            }
        });
        return convertView;
    }

    // 展示图片
    private void showBigImgs(final int position) {
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
        iver.overlayStatusBar(true) // ImageViewer 是否会占据 StatusBar 的空间
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
            public boolean onItemClick(final int position, ImageView imageView) { // 将展示修改界面
                if (rlIverShow.getVisibility() == View.VISIBLE){
                    rlIverShow.setVisibility(View.GONE);    // 展示界面消失
                    rlIverEdit.setVisibility(View.VISIBLE);
                    iver.setBackgroundColor(Color.parseColor("#ffffff"));  // 背景色变白
                    // 展示修改界面显示
                    ivEditExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iver.cancel();
                        }
                    });
                    String time = dataSource.get(position).getPtime();
                    if (null != time){
                        tvEditTime.setText(time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日");
                    }else{
                        tvEditTime.setText("");
                    }
                    ivEditDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowPhotoInfoDialog dialog = ShowPhotoInfoDialog.getDialog();
                            dialog.setContext(context);
                            dialog.setPhoto(dataSource.get(position));
                            dialog.showBottomDialog();
                        }
                    });
                    ivEditDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteDialog(position);
                        }
                    });
                }else if (rlIverEdit.getVisibility() == View.VISIBLE){  // 将展示展示界面
                    rlIverEdit.setVisibility(View.GONE);
                    rlIverShow.setVisibility(View.VISIBLE); // 展示界面显示
                    iver.setBackgroundColor(Color.parseColor("#000000"));// 背景色变黑
                    // 修改comment
                    String comment = dataSource.get(position).getPdescribe();
                    if (null != comment && !comment.equals(""))
                        tvComment.setText(comment);
                }
                return true;    // 返回值为true，不会退出展示
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
                    rlIverShow.setVisibility(View.VISIBLE);
                    rlIverEdit.setVisibility(View.GONE);    // 进来时展示展示界面
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    // 此时未开启预览图片
                    rlOut.setVisibility(View.VISIBLE);
                    rcv.setVisibility(View.VISIBLE);
                    rlIverShow.setVisibility(View.GONE);
                    rlIverEdit.setVisibility(View.GONE);
                }
            }
        });
    }
    private void showDeleteDialog(final int position){
        // 显示一个Dialog
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
        adBuilder.setTitle("确定删除此图片");

        adBuilder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 1. 删除数据源
                int id = dataSource.get(position).getId();
                dataSource.remove(position);
                showImgSource.remove(position);
                notifyDataSetChanged();
                // 2. 关闭iver
                iver.cancel();
                tvCount.setText(dataSource.size()+"");
                // 3. 修改数据库
                changeDelete(id);
            }
        });
        adBuilder.setNegativeButton("我手滑了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 选中“取消”按钮，取消界面
            }
        });
        adBuilder.create().show();
    }
    private static final String IP = Constant.IP + "/photo/delete";
    private void changeDelete(int id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder().add("photoId",id+"");
        FormBody body = builder.build();
        Request request = new Request.Builder().post(body).url(IP).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Toast.makeText(context,"delete失败",Toast.LENGTH_SHORT).show();
                Log.e("deleteee","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String strings = response.body().string();
//                Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                Log.e("deletee","成果");
            }
        });
    }
    private class ViewHolder {
        RelativeLayout rlOut;
        ImageView ivImg;
    }
}
