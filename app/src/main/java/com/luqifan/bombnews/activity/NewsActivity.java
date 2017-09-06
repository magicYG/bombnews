package com.luqifan.bombnews.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luqifan.bombnews.R;
import com.luqifan.bombnews.fragment.NewsListFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sp;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] mTitleArray = {"推荐", "时尚", "游戏", "体育", "动漫"};
    TabPagerAdapter adapter;
    private com.luqifan.bombnews.db.ShiningFingerLite ShiningFingerLite;
    private List<View> views;
    private TextView main_tab_new_message;
    SQLiteDatabase db;
    int i = 0;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private String TAG="NewsActivity";

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            URL url = new URL("http://c.m.163.com/nc/article/headline/T1348647909107/0-10.html");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //设置请求中的媒体类型信息。
            conn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            conn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 获取返回的数据
                String result = streamToString(conn.getInputStream());
                Log.d(TAG, "run: result="+result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}).start();

        ImageView LoginimageView = (ImageView) findViewById(R.id.LoginimageView);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        List<Fragment> mFragments = new ArrayList<>();
//        为每个布局添加java文件
        for (int i1 = 0; i1 < mTitleArray.length; i1++) {
            mFragments.add(new NewsListFragment(this));
        }
        viewPager = (ViewPager) findViewById(R.id.vp);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(),mFragments);
        viewPager.setAdapter(adapter);

        // TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tb);
        tabLayout.setupWithViewPager(viewPager);      // ViewPager <---> TabLayout

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);


        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        /**
//         * 第 1 步: 检查是否有相应的权限
//         */
//        boolean isAllGranted = checkPermissionAllGranted(
//                new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                }
//        );
//        // 如果这3个权限全都拥有, 则直接执行备份代码
//        if (isAllGranted) {
//            doBackup();
//            return;
//        }
//        /**
//         * 第 2 步: 请求权限
//         */
//        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{
////                        地理位置权限
//                        Manifest.permission.ACCESS_FINE_LOCATION,
////                        通讯录权限
//                        Manifest.permission.READ_CONTACTS,
////                        读存储卡权限
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
////                        写存储卡权限
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                },
//                MY_PERMISSION_REQUEST_CODE
//        );
    }
//    private void doBackup() {
//        // 本文主旨是讲解如果动态申请权限, 具体备份代码不再展示, 就假装备份一下
////        Toast.makeText(this, "认证完毕", Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 检查是否拥有指定的所有权限
//     */
//    private boolean checkPermissionAllGranted(String[] permissions) {
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                // 只要有一个权限没有被授予, 则直接返回 false
//                Log.i("permission权限：" + permission, "禁止");
//                return false;
//            } else {
//                Log.i("permission权限：" + permission, "允许");
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 第 3 步: 申请权限结果返回处理
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
//            boolean isAllGranted = true;
//
//            // 判断是否所有的权限都已经授予了
//            for (int grant : grantResults) {
//                if (grant != PackageManager.PERMISSION_GRANTED) {
//                    isAllGranted = false;
//                    break;
//                }
//            }
//
//            if (isAllGranted) {
//                // 如果所有的权限都授予了, 则执行备份代码
//                doBackup();
//
//            } else {
//                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                openAppDetails();
//            }
//        }
//    }
//
//    private void openAppDetails() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("获取地理位置需要访问 “GPS”，请到 “应用信息 -> 权限” 中授予！");
//        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                startActivity(intent);
//            }
//        });
//        builder.setNegativeButton("取消", null);
//        builder.show();
//    }
//        MenuView.ItemView nav_camera=(MenuView.ItemView)findViewById(R.id.nav_camera);
//        getMenuInflater().inflate(R.menu.main,);



    //双击返回键可以退出
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        获取当前运行activity
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();


        if (id == R.id.nav_camera) {
            if (runningActivity.equals("com.luqifan.bombnews.activity.NewsActivity")){



            }else {
                Intent intent = new Intent(NewsActivity.this, NewsActivity.class);
                startActivity(intent);
                finish();
            }
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(NewsActivity.this,LoginActivity.class);
            startActivity(intent);
            item.setCheckable(false);


        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(NewsActivity.this,LocationActivity.class);
            startActivity(intent);
            item.setCheckable(false);

        } else if (id == R.id.nav_manage) {
            item.setCheckable(false);

        } else if (id == R.id.nav_share) {
            item.setCheckable(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // 自定义ViewPager页面适配器
    public class TabPagerAdapter extends FragmentPagerAdapter {


        private final List<Fragment> mFragments;

        public TabPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> mFragments) {
            super(supportFragmentManager);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        //        获取tablayout的标签
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleArray[position];
        }
    }





}