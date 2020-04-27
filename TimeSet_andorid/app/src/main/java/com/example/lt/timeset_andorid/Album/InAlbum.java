package com.example.lt.timeset_andorid.Album;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.lt.timeset_andorid.R;
import java.util.HashMap;
import java.util.Map;
public class InAlbum extends AppCompatActivity {
    private ImageButton return0;
    private ImageButton btn_search;
    LinearLayout layout1;
    LinearLayout layout2;


    private Map<String, MyTabSpec> map = new HashMap<>();
    private String [] tabStrId = {"时间相册", "足迹地球"};
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album);
        initData();
        findView();
        setListener();
        changeFragment(tabStrId[0]);
    }
    private void initData() {
        map.put(tabStrId[0], new MyTabSpec());
        map.put(tabStrId[1], new MyTabSpec());
        map.get(tabStrId[0]).setFragment(new CalendarFragment());
        map.get(tabStrId[1]).setFragment(new MapFragment());
    }
    private void findView() {
        layout1 = findViewById(R.id.tab_spec_1);
        layout2 = findViewById(R.id.tab_spec_2);
        return0=findViewById(R.id.btn_return1);
        btn_search=findViewById(R.id.btn_search);
    }
    private void setListener() {
        MyListener listener = new MyListener();
        layout1.setOnClickListener(listener);
        layout2.setOnClickListener(listener);
        return0.setOnClickListener(listener);
        btn_search.setOnClickListener(listener);
    }
    private class MyTabSpec {
        private Fragment fragment = null;
        public Fragment getFragment() {
            return fragment;
        }
        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }
    private void changeFragment(String s) {
        Fragment fragment = map.get(s).getFragment();
        if(curFragment == fragment) return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        if(curFragment!=null)
            transaction.remove(curFragment);
        if(!fragment.isAdded()) {
            transaction.add(R.id.tab_content, fragment);
        }
        transaction.show(fragment);
        curFragment = fragment;
        transaction.commit();
    }
    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tab_spec_1:
                    changeFragment(tabStrId[0]);
                    break;
                case R.id.tab_spec_2:
                    changeFragment(tabStrId[1]);
                    break;

            }
        }
    }
}









