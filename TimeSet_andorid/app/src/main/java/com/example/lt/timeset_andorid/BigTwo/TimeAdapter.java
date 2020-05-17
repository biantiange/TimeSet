package com.example.lt.timeset_andorid.BigTwo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lt.timeset_andorid.R;

import java.util.List;
import java.util.Map;

public class TimeAdapter extends BaseAdapter {
    private List<Map<String, Object>> mList;
    private Context mContext;

    public TimeAdapter(Context context, List<Map<String, Object>> mListBean) {
        this.mList = mListBean;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.list_time, null);
            /* holder.time = (TextView) convertView.findViewById(R.id.time_tv);*/
            holder.msg = (TextView) convertView.findViewById(R.id.msg_tv);
            holder.up = (View) convertView.findViewById(R.id.line_up);
            holder.down = (View) convertView.findViewById(R.id.line_dowm);
            holder.quan = (ImageView) convertView.findViewById(R.id.quan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s=mList.get(position).get("time").toString();
         holder.msg.setText(s.substring(0,4)+"年"+s.substring(4,6)+"月");
        final ImageView image = convertView.findViewById(R.id.quan);
        if ((Integer) mList.get(position).get("statu") == 1) {
            Log.e("Statu", "Statu");
            image.setImageResource(R.drawable.check);
            mList.get(position).put("statu", 0);

        } else {
            image.setImageResource(R.drawable.circle);
        }
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
        String time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String msg = mList.get(position);
        holder.time.setText(time);

        holder.msg.setText(msg);*/
        return convertView;
    }


    private static class ViewHolder {
        /* private TextView time;*/
        private TextView msg;
        private View up;
        private View down;
        private ImageView quan;
    }
}