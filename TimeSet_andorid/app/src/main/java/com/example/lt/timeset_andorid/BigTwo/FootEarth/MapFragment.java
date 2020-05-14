package com.example.lt.timeset_andorid.BigTwo.FootEarth;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.lt.timeset_andorid.BigTwo.InAlbumActivity;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.Photo;
import com.example.lt.timeset_andorid.BigTwo.TimePhoto.PhotoList;
import com.example.lt.timeset_andorid.R;
import com.example.lt.timeset_andorid.util.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 赵宁：足迹地球
 */
public class MapFragment extends Fragment {

    private int albumId;
    private int userId;
    private SharedPreferences sharedPreferences;//获取用户信息

    private List<PhotoList> plToAdaper;     // 获取数据源
    private Map<String, List<PhotoList>> photoMap = new HashMap<>();

    // 请求定位
    private final int REQUEST_GPS = 1;
    private static final String TITLE_MYSELF = "myself";
    // 百度地图
    private MapView mapView = null;
    private View newView = null;
    private BaiduMap baiduMap = null;
    private LocationClient locationClient = null;   // 定位服务
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //对百度地图SDK做初始化
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        //参数： 环境上下文，最好是应用程序级别的上下文
        SDKInitializer.initialize(this.getActivity().getApplicationContext());
        newView = inflater.inflate(R.layout.in_album_map, container, false);
        //获取地图控件引用
        mapView = newView.findViewById(R.id.map);
        baiduMap = mapView.getMap();
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDataByOkHTTP();
//        getData();
//        initPlToMark(plToAdaper);
//        hideBaiduLogo();
//        // 比例尺设置为市级
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(8.0f);
//        baiduMap.setMapStatus(msu);
//        // 定位自己
//        locationClient = new LocationClient(getContext());
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
//        // 给所有图片覆盖到地图上
//        setAllPhotoToBaiduMap();
//        // 添加标注物点击监听器
//        addItemTouchListener();
    }

    private void getData() {
        // 1. 获取照片数据( 模拟通过intent获取phonoList数据源)
        plToAdaper = new ArrayList<>();
        Photo photo = new Photo();
        photo.setPlace("北京");
        photo.setPtime("20200202");
        photo.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
        Photo photo1 = new Photo();
        Photo photo4 = new Photo();
        photo4.setPlace("北京");
        photo4.setPtime("20200202");
        photo4.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");
        Photo photo5 = new Photo();
        photo5.setPlace("北京");
        photo5.setPtime("20200202");
        photo5.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");

        Photo photo6 = new Photo();
        photo6.setPlace("北京");
        photo6.setPtime("20200202");
        photo6.setPath("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg");

        photo1.setPlace("上海");
        photo1.setPtime("20200202");
        photo1.setPath("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
        Photo photo2 = new Photo();
        photo2.setPlace("广西");
        photo2.setPtime("20200203");
        photo2.setPath("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3252521864,872614242&fm=26&gp=0.jpg");
        Photo photo3 = new Photo();
        photo3.setPlace("成都");
        photo3.setPtime("20200204");
        photo3.setPath("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3252521864,872614242&fm=26&gp=0.jpg");
        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo);
        photoList.add(photo1);
        photoList.add(photo4);
        photoList.add(photo5);
        photoList.add(photo6);
        List<Photo> photos1 = new ArrayList<>();
        photos1.add(photo2);
        List<Photo> photos2 = new ArrayList<>();
        photos2.add(photo3);
        PhotoList photoList1 = new PhotoList();
        photoList1.setPhotoList(photoList);
        photoList1.setPtime("20200202");
        PhotoList photoList2 = new PhotoList();
        photoList2.setPhotoList(photos1);
        photoList2.setPtime("20200203");
        PhotoList photoList3 = new PhotoList();
        photoList3.setPhotoList(photos2);
        photoList3.setPtime("20200204");
        plToAdaper.add(photoList1);
        plToAdaper.add(photoList2);
        plToAdaper.add(photoList3);
    }

    private void initDataByOkHTTP() {
        albumId = getActivity().getIntent().getIntExtra("albumId", -1);
        sharedPreferences = getContext().getSharedPreferences("user", MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", -1);
        OkHttpClient okHttpClient = new OkHttpClient();
        if (albumId != -1 && userId != -1) {
            FormBody.Builder builder = new FormBody.Builder().add("albumId", String.valueOf(albumId)).add("userId", String.valueOf(userId));
            FormBody body = builder.build();
            Request request = new Request.Builder().post(body).url(Constant.URL + "/photo/findByAlbum").build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String strings = response.body().string();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = strings;

                    handler.sendMessage(message);
                }
            });
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.e("======", "执行失败");
            }
            if (msg.what == 2) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                plToAdaper = gson.fromJson(msg.obj.toString(), new TypeToken<List<PhotoList>>() {
                }.getType());
                initPlToMark(plToAdaper);
                hideBaiduLogo();
                // 比例尺设置为市级
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(8.0f);
                baiduMap.setMapStatus(msu);
                // 定位自己
                locationClient = new LocationClient(getContext());
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
                // 给所有图片覆盖到地图上
                setAllPhotoToBaiduMap();
                // 添加标注物点击监听器
                addItemTouchListener();
            }

        }
    };
    //  将获取的数据源封装成一个可以显示在地图上的List
    private void initPlToMark(List<PhotoList> plToAdaper) {
        // 根据photoList里边每一个 list，按着地点封装进map
        List<Photo> allPhoto = new ArrayList<>();
        for (PhotoList photoList:plToAdaper){
            allPhoto.addAll(photoList.getPhotoList());
        }
        // 将数据源封装入Map
        for (Photo photo : allPhoto) {
            String place = photo.getPlace();
            if (photoMap.containsKey(place)) {   // 先判断photoMap中是否含有相应地点的key
                List<PhotoList> photoLists = photoMap.get(place);
                // 此时含有对应key 地点
                if (null == photoLists) {
                    // 如果这个为对应list的第一个数据
                    photoLists = new ArrayList<>();
                    PhotoList photoList = new PhotoList();
                    photoList.setPtime(photo.getPtime());   // 创建time
                    List<Photo> photos = new ArrayList<>();
                    photos.add(photo);
                    photoList.setPhotoList(photos);
                    photoLists.add(photoList);
                } else {
                    boolean haveTime = false;
                    for (int i=0;i<photoLists.size();i++){
                        if (photoLists.get(i).getPtime().equals(photo.getPtime())){
                            photoLists.get(i).getPhotoList().add(photo);
                            haveTime = true;
                            break;
                        }
                    }
                    if (!haveTime){
                        PhotoList photoList = new PhotoList();
                        photoList.setPtime(photo.getPtime());
                        List<Photo> photos = new ArrayList<>();
                        photos.add(photo);
                        photoList.setPhotoList(photos);
                    }
                }
                photoMap.put(place, photoLists);
            } else {  // 此时不含有对应key
                List<PhotoList> photoLists = new ArrayList<>(); // 创建对应的 PhotoList
                PhotoList photoList = new PhotoList();
                photoList.setPtime(photo.getPtime());   // 创建time
                List<Photo> photos = new ArrayList<>();
                photos.add(photo);
                photoList.setPhotoList(photos);
                photoMap.put(place, photoLists);
            }
        }
    }

    // 给所有图片覆盖到地图上
    private void setAllPhotoToBaiduMap() {
        for (String city : photoMap.keySet()) {
            new GetLatLngTask(city).execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GPS) {    // GPS权限申请成功

            // 2. 设置定位参数
            LocationClientOption locationClientOption = new LocationClientOption();
            // 设置打开GPS  ----    动态申请 GPS 权限
            locationClientOption.setOpenGps(true);
            // 选用坐标系：gcj02  bd09  bd09ll( 常用 )
            locationClientOption.setCoorType("bd09ll");
            // 三种定位模式：高精度定位、低功耗定位（不使用GPS，使用网络定位）、仅用设备定位（只用GPS）
            locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 当前最好使用网络定位
            // 设置定位结果中，需要字符串地址
            locationClientOption.setIsNeedAddress(true);
            //设置定位结果中需要Poi
            locationClientOption.setIsNeedLocationPoiList(true);

            // 3. 将设置好的参数应用到 locationClient中
            locationClient.setLocOption(locationClientOption);

            // 4. 给定位客户端注册一个接口
            locationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    // 当 定位成功时回调
                    // BDLocation 中包含定位结果数据
                    Log.e("定位状态", bdLocation.getLocType() + "");
                    Log.e("定位结果", "纬度：" + bdLocation.getLatitude()
                            + ", 经度：" + bdLocation.getLongitude()
                            + ", 地址：" + bdLocation.getAddrStr()
                            + ", 国家：" + bdLocation.getCountry()
                            + ", 城市：" + bdLocation.getCity()
                            + ", 区/县：" + bdLocation.getDistrict());

                    // 获取当前位置周围的信息点
                    List<Poi> poiList = bdLocation.getPoiList();
                    if (null != poiList) {
                        for (int i = 0; i < poiList.size(); i++) {
                            Log.e("周边名称", poiList.get(i).getName());
                            Log.e("周边位置", poiList.get(i).getAddr());
                        }
                    }

                    // 添加标注覆盖物
                    // 从 bdLocation 中获取经纬度值
                    LatLng latLng1 = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                    // 获取覆盖物图标 ===
                    BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.location);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng1)
                            .title(TITLE_MYSELF)
                            .icon(descriptor);
                    // 显示
                    baiduMap.addOverlay(markerOptions);

                    // 移动地图，显示当前位置
                    MapStatusUpdate move = MapStatusUpdateFactory.newLatLng(latLng1);  // 移动新坐标点
                    baiduMap.animateMapStatus(move);

                    // 创建定位数据对象，让定位图层显示
                    MyLocationData data = new MyLocationData.Builder()
                            .latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude())
                            .direction(bdLocation.getDirection())   // 方向
                            .build();
                    // 地图中添加定位数据
                    baiduMap.setMyLocationData(data);

                }
            });

            // 5. 启动定位
            locationClient.start();
        }
    }

    // 获取覆盖物控件
    private View getMarkView(List<PhotoList> imgRes) {
        View markView = LayoutInflater.from(getContext()).inflate(R.layout.foot_earth_mark_item, null);
        TextView tvCount = markView.findViewById(R.id.tv_foot_earth_mark_item_count);
        ImageView ivCover = markView.findViewById(R.id.iv_foot_earth_mark_item_cover);
        if (imgRes.size() == 1) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
        }
        Glide.with(this)
                .load(imgRes.get(0).getPhotoList().get(0).getPath())
                .into(ivCover);
        tvCount.setText(imgRes.size() + "");
        if (imgRes.size() < 10) {
            tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);
        } else if (imgRes.size() < 100) {
            tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
        } else {
            tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
        }
        return markView;
    }

    class GetLatLngTask extends AsyncTask {
        private String city;

        public GetLatLngTask(String city) {
            this.city = city;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<Address> addressList = null;
            if (city != null) {
                Geocoder gc = new Geocoder(getContext(), Locale.CHINA);
                Log.e("输出", "0000" + city);
                try {
                    addressList = gc.getFromLocationName(city, 1);
                    Log.e("输出", "1111" + city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return addressList;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            List<Address> addressList = (List<Address>) o;
            LatLng latLng = null;
            if (!addressList.isEmpty()) {
                Address address_temp = addressList.get(0);
                //计算经纬度
                latLng = new LatLng(address_temp.getLongitude(), address_temp.getLatitude());
                Log.e("输出", "1111111" + city);
                System.out.println("经度：" + latLng.latitude);
                System.out.println("纬度：" + latLng.longitude);
            }
            addMarkOverlay(latLng, city, photoMap.get(city));
        }
    }


    // 添加地图覆盖物：标注覆盖物
    private void addMarkOverlay(LatLng latLng1, String city, final List<PhotoList> imgResource) {
        // 1. 脱裤子放屁，但不这样就出错
        LatLng latLng = new LatLng(latLng1.longitude, latLng1.latitude); // 纬度、精度
        // 2. 创建标注覆盖物选项对象
        BitmapDescriptor descriptor1 = BitmapDescriptorFactory.fromView(getMarkView(imgResource));
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(descriptor1)     // 指定 添加的图片
                .title(city)
                .position(latLng)       // 指定  添加的位置
                .draggable(true);      // 指定  标志物可以被拖拽

        // 3. 添加到地图显示   =====   返回值类型即为 标志位本身
        baiduMap.addOverlay(markerOptions);
    }

    private void addItemTouchListener() {
        // 4. 给覆盖物添加点击监听事件    标志物需要设置可拖拽
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.getTitle().equals(TITLE_MYSELF)) {
                    Intent intent = new Intent(getContext(), ShowFootPhotoActivity.class);
                    intent.putExtra("photos", new Gson().toJson(photoMap.get(marker.getTitle())));
                    intent.putExtra("city",marker.getTitle());
                    startActivity(intent);
                }
                return false;
            }
        });
    }
    // 隐藏百度Logo
    private void hideBaiduLogo() {
        // 隐藏baiduMap logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView)) {
            // 子视图不为空，而且子视图为 ImageView 的一个实例
            child.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    public void onResume () {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause () {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

}
