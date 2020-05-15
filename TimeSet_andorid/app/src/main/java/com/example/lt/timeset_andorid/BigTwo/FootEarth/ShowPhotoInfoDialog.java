package com.example.lt.timeset_andorid.BigTwo.FootEarth;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lt.timeset_andorid.Entity.Photo;
import com.example.lt.timeset_andorid.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowPhotoInfoDialog {
    private static ShowPhotoInfoDialog showPhotoInfoDialog;

    private ShowPhotoInfoDialog() {
    }

    public static ShowPhotoInfoDialog getDialog() {
        if (showPhotoInfoDialog == null)
            synchronized (ShowPhotoInfoDialog.class) {
                if (null == showPhotoInfoDialog) {
                    showPhotoInfoDialog = new ShowPhotoInfoDialog();
                }
            }
        return showPhotoInfoDialog;
    }

    private Dialog dialog;
    private Context context;// 上下文
    private Photo photo;    // 数据源


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    // 生成dialog
    public void showBottomDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(context, R.layout.look_photo_information_pop, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        ImageView ivExit = dialog.findViewById(R.id.iv_look_photo_info_exit);
        TextView tvChangeOk = dialog.findViewById(R.id.tv_look_photo_info_change_ok);
        TextView tvTime = dialog.findViewById(R.id.tv_look_photo_time_set);
        TextView tvPlace = dialog.findViewById(R.id.tv_look_photo_place_set);
        TextView tvAlbum = dialog.findViewById(R.id.tv_look_photo_album_set);
        final EditText etDescribe = dialog.findViewById(R.id.et_look_photo_describe);
        String time = photo.getPtime();
        if (null != time && !time.equals("")) {
            tvTime.setText(time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日");
        } else {
            tvTime.setText("未知");
        }
        if (null != photo.getPlace() && !photo.getPlace().equals("")) {
            tvPlace.setText(photo.getPlace());
        } else {
            tvPlace.setText("未知");
        }
        // todo: 获取相册的名称
        tvAlbum.setText(photo.getAlbumId() + "");
        if (photo.getPdescribe() != null) {
            etDescribe.setText(photo.getPdescribe());
        }

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvChangeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setPdescribe(etDescribe.getText().toString());
                changeUpdate(etDescribe.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private static final String IP = "photo/updatePhotoDescription";
    private void changeUpdate(String newDescribe) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder().add("newDescribe",newDescribe).add("photoId",photo.getId()+"");
        FormBody body = builder.build();
        Request request = new Request.Builder().post(body).url(IP).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Toast.makeText(context,"update失败",Toast.LENGTH_SHORT).show();
                Log.e("updateee","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String strings = response.body().string();
//                Toast.makeText(context,"2",Toast.LENGTH_SHORT).show();
                Log.e("updateee","成果");
            }
        });
    }

}
