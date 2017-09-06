package com.luqifan.bombnews.activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luqifan.bombnews.R;
import com.luqifan.bombnews.db.ShiningFingerLite;

/**
 * Created by 陆启凡 on 2017/9/1.
 */

public class SetPasswordActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private ShiningFingerLite ShiningFingerLite;
    //    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        ShiningFingerLite = new ShiningFingerLite(this, "Student.db", null, 1);
        final EditText et_setusername = (EditText) findViewById(R.id.et_setusername);
        final EditText et_setpassword = (EditText) findViewById(R.id.et_setpassword);
        final EditText et_setpassword_again = (EditText) findViewById(R.id.et_setpassword_again);

        Button et_btn_ok = (Button) findViewById(R.id.et_btn_ok);
        Button et_btn_cancel = (Button) findViewById(R.id.et_btn_cancel);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("first", true)) {
            ShiningFingerLite.getWritableDatabase();
        }

        //确定按钮的单机事件
        et_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = et_setpassword.getText().toString().trim();
                String username = et_setusername.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SetPasswordActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SetPasswordActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                String confirm_password = et_setpassword_again.getText().toString().trim();
                if (TextUtils.equals(password, confirm_password)) {
                    //成功！  保存密码
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.putString("password", password);
                    edit.commit();

//                  values存放输入的用户名和密码
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", password);
                    //insert（）方法中第一个参数是表名，第二个参数是表示给表中未指定数据的自动赋值为NULL。第三个参数是一个ContentValues对象
                    ShiningFingerLite.getWritableDatabase().insert("Student", null, values);
//                    清空values的值
                    values.clear();

                    Toast.makeText(SetPasswordActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();


                } else {
                    Toast.makeText(SetPasswordActivity.this, "两次输入的密码不同", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        //取消按钮的单击事件
        et_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
