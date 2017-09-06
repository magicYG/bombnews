package com.luqifan.bombnews.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.luqifan.bombnews.R;

//import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类


/**
 * Created by Administrator on 2017/7/20.
 */

public class LocationActivity extends Activity {
    private TextView textView;
    private LocationClient locationClient = null;
    private Button button = null;
    public MyBdlocationListener myListener = new MyBdlocationListener();
    private String TAG = "TestActivity";
    private TextView textView7;
    private boolean b1;
//    String city1=null;


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    public class MyBdlocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            final String addrStr = location.getAddrStr();
            final String city = location.getCity();
//            city1=city;
            boolean hasAddr = location.hasAddr();
            final String locationDescribe = location.getLocationDescribe();
            Log.e(TAG, "addrStr=" + addrStr + "locationDescribe=" + locationDescribe);
            //            textView.setText(addrStr);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp;
                    sp = getSharedPreferences("config", MODE_PRIVATE);
                    textView.setText("您所在的城市:" + city + "  地址:" + addrStr + locationDescribe);
                    b1 = city.equals("上海市");
                }
            });

        }

//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//
//            Log.e(TAG, "onConnectHotSpotMessage: s" + s);
//        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final SharedPreferences sp;
        sp = getSharedPreferences("config", MODE_PRIVATE);

        //注册监听函数
        textView = findViewById(R.id.textView3);
        textView7 = findViewById(R.id.textView7);
        button = findViewById(R.id.button);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();//初始化LocationgClient
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationClient.isStarted()) {
                    locationClient.stop();
                }
                locationClient.start();
                if (b1) {

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("Isfirstin", true);
                    edit.commit();
                    Toast.makeText(LocationActivity.this, "当前的位置是上海市 已复制到粘贴栏", Toast.LENGTH_SHORT).show();
                    textView7.setText("上海市");
//                    复制文本到粘贴栏操作
                    ClipboardManager copy = (ClipboardManager) LocationActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    String text1=textView7.getText().toString().trim();
                    copy.setText(text1);
                }
//                if (city1.equals("上海市")){
//                    textView7.setText("上海市");
//                }else {
//                    textView7.setText("无");
//                }
            }
        });

    }
}

