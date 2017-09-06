package com.luqifan.bombnews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

//创建数据库
public class ShiningFingerLite extends SQLiteOpenHelper {
    //方便后期我们在实现数据库操作的时候能方便去使用表名,同时也方便后期去修改表名
    public static final String DB_NAME = "info";
    public static final int DATABASE_VERSION = 1;
//    public static final String FIELD_ID="id";
//    private final static String TABLE_NAME="username";
//    public final static String FIELD_TITLE="sec_Title";

    /**
     * context : 上下文
     * name :　数据库名称
     * factory：游标工厂
     * version : 数据库的版本号
     *
     * @param context
     */
    private Context mContext;

    public ShiningFingerLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    //第一次创建数据库的调用,创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构   字段:   blacknum:黑名单号码     mode:拦截类型
        //参数:创建表结构sql语句


//        integer — 整型 real—浮点型 text—文本类型 blob—二进制类型
        //CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name varchar(20), age INTEGER)");
        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Student.TABLE + "(studentid INTEGER PRIMARY KEY AUTOINCREMENT," + Student.KEY_username + " INTEGER,"
                + Student.KEY_password + " INTEGER," + Student.KEY_message + " VARCHAR(50))";
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL("insert into Student(message1) values('测试数据1')");

        Toast.makeText(mContext, "请创建你的账户", Toast.LENGTH_SHORT).show();

    }

    //当数据库版本发生变化的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
//        db.execSQL("DROP TABLE IF EXISTS "+ Student.TABLE);
//
//        //再次创建表
//        onCreate(db);
    }


//    public Cursor select()
//    {
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null,  " id desc");
//        return cursor;
//    }
//
//    public long insert(String Title)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues cv=new ContentValues();
//        cv.put(FIELD_TITLE, Title);
//        long row=db.insert(TABLE_NAME, null, cv);
//        return row;
//    }
//
//    public void delete(int id)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        String where=FIELD_ID+"=?";
//        String[] whereValue={Integer.toString(id)};
//        db.delete(TABLE_NAME, where, whereValue);
//    }


}