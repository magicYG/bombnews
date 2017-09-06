package com.luqifan.bombnews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luqifan.bombnews.R;
import com.luqifan.bombnews.db.ShiningFingerLite;

/**
 * Created by Administrator on 2017/8/25.
 */

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private ShiningFingerLite ShiningFingerLite;
    //    private SQLiteDatabase db;
    private Cursor cursor;
    //    SQLiteDatabase db;
    int count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sp = getSharedPreferences("config", MODE_PRIVATE);
        ShiningFingerLite = new ShiningFingerLite(this, "Student.db", null, 1);
        final EditText et_enterusername = (EditText) findViewById(R.id.et_enterusername);
        final EditText et_enterpassword = (EditText) findViewById(R.id.et_enterpassword);
        ImageView et_enterpassword_hide = (ImageView) findViewById(R.id.et_enterpassword_hide);
        Button et_btn_ok = (Button) findViewById(R.id.et_btn_ok);
        Button et_btn_cancel = (Button) findViewById(R.id.et_btn_cancel);

        et_enterpassword_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count % 2 == 0) {
                    //显示密码  设置输入类型  attrs文件中找到inputType:none,password  将其16进制转为10进制
                    et_enterpassword.setInputType(0);
                } else {
                    //隐藏密码
                    et_enterpassword.setInputType(129);
                }
                count++;
            }
        });


//        登陆事件
        et_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_enterusername.getText().toString().trim();
                String password = et_enterpassword.getText().toString().trim();
//                String sp_username = sp.getString("username", "");
//                String sp_password = sp.getString("password", "");
                Cursor cursor = ShiningFingerLite.getWritableDatabase().rawQuery("select * from Student", null);
//                    cursor.moveToFirst();
//                指针移到表末尾
                cursor.moveToLast();
                int a;
//                从末尾到开头遍历表
                for (a = cursor.getCount(); a >= 0; a--) {
//                    获取用户名 cursor.getString(列数)
                    String u = cursor.getString(1);
//                    获取密码
                    String p = cursor.getString(2);
//                    判断用户名和密码是否正确
                    if (username.equals(u) & password.equals(p)) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
//                        Intent intent3 = new Intent(LoginActivity.this, NewsActivity.class);
//                        startActivity(intent3);
//                        return防止重复打开页面
                        return;
                    } else {
//                        进行多次判断  指针到表头时说明没有用户名和密码
                        if (cursor.moveToFirst()) {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        } else {
                            cursor.moveToPrevious();
                        }

                    }
                }
                if (username.isEmpty() & password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
//                sp登录
//                if (username.equals(sp_username) & password.equals(sp_password)) {
//                    //成功登录
//                    Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                    Intent intent3 = new Intent(HomeActivity.this, LostfindActivity.class);
//                    startActivity(intent3);
//                } else {
//                    Toast.makeText(HomeActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }
        });


        et_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SetPasswordActivity.class);
                startActivity(intent);
            }
        });


    }
    }




