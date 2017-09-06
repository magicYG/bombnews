package com.luqifan.bombnews.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.luqifan.bombnews.R;
import com.luqifan.bombnews.db.ShiningFingerLite;


public class SplashActivity extends AppCompatActivity {
    private static final int MSG_UPDATE_DIALOG = 1;
    private SharedPreferences sp;
    private String code;
    private com.luqifan.bombnews.db.ShiningFingerLite ShiningFingerLite;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DIALOG:
                    showdialog();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ShiningFingerLite = new ShiningFingerLite(this, "Student.db", null, 1);
//        final SharedPreferences.Editor edit = sp.edit();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        TextView tv_versionname = (TextView) findViewById(R.id.tv_versionname);
        tv_versionname.setText("版本号:" + getVersionname());
        if (sp.getBoolean("first", true)) {
            ShiningFingerLite.getWritableDatabase();
        }
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            new Thread() {
                public void run() {
                    SystemClock.sleep(2000);
                    enerHome();
                }
            }.start();
        }



        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            doBackup();
            return;
        }
        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[]{
//                        地理位置权限
                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        通讯录权限
                        Manifest.permission.READ_CONTACTS,
//                        读存储卡权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        写存储卡权限
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    //显示对话框
    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前最新版本" + getVersionname());
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(code + "是目前的版本号哦");
        //ok按钮
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                download();
            }
        });
        //cancel按钮
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                enerHome();
            }
        });
        builder.show();
    }

    private void download() {
        final SharedPreferences.Editor edit = sp.edit();
        Toast.makeText(this, "已经是最新版本了哦", Toast.LENGTH_SHORT).show();
        if(sp.getBoolean("Isfirstin",true)) {
            //保存状态
            edit.putBoolean("Isfirstin", false);
            // 向preferences文件中提交数据：
            edit.commit();
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        }
        finish();
    }



    private void doBackup() {
        // 本文主旨是讲解如果动态申请权限, 具体备份代码不再展示, 就假装备份一下
//        Toast.makeText(this, "认证完毕", Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                Log.i("permission权限：" + permission, "禁止");
                return false;
            } else {
                Log.i("permission权限：" + permission, "允许");
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                doBackup();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("获取地理位置需要访问 “GPS”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    private void enerHome() {
        final SharedPreferences.Editor edit = sp.edit();
        if(sp.getBoolean("Isfirstin",true)) {
            //保存状态
            edit.putBoolean("Isfirstin", false);
            // 向preferences文件中提交数据：
            edit.commit();
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);

        }
        finish();
    }

    //子线程
    private void update() {
        new Thread() {
            private int startTime;

            public void run() {
                Message message = Message.obtain();
                startTime = (int) System.currentTimeMillis();
                code = getVersionname();
                if (code.equals(getVersionname())) {
                    message.what = MSG_UPDATE_DIALOG;
                    int endTime = (int) System.currentTimeMillis();
                    int dTime = endTime - startTime;
                    //弹出对话框
                    if (endTime - startTime < 2000) {
                        SystemClock.sleep(2000 - dTime);
                    } else {
                    }
                    handler.sendMessage(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
//                    Android子线程中是不能直接弹出Toast，因为子线程中没有Looper
//                    Toast.makeText(SplashActivity.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        }.start();
    }

    //获取版本号
    private String getVersionname() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.example.administrator.myapplication", 0);
            String versionname = packageInfo.versionName;
            return versionname;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}