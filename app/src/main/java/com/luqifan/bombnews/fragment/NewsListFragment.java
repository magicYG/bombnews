package com.luqifan.bombnews.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.luqifan.bombnews.MyListView;
import com.luqifan.bombnews.R;
import com.luqifan.bombnews.db.ShiningFingerLite;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 陆启凡 on 2017/9/4.
 */

public class NewsListFragment extends Fragment {
    private final Context context;
    private ListView gv_lostfind_listview;
    private List<String> data;
    private BaseAdapter adapter;
    private com.luqifan.bombnews.db.ShiningFingerLite ShiningFingerLite;
    View view;
    ImageView imageView;
    private String TAG = "NewsListFragment";

    public NewsListFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        RetrofitClient.getRetrofit(getContext()).create(NewsApi.class)
//                .getData()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<News>>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted: ");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: error=" + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<News> newses) {
//
//                        Log.d(TAG, "onNext: newses=" + newses);
//                    }
//                });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutRes(), container, false);
        ShiningFingerLite = new ShiningFingerLite(getContext(), "Student.db", null, 1);
//        sp与context一样属于上下文
//        把context转变为sharedpreferences才能够用到sp的方法
        SharedPreferences config = context.getSharedPreferences("config", MODE_PRIVATE);

        if (config.getBoolean("first", true)) {
            ShiningFingerLite.getWritableDatabase();
        }
        data = new ArrayList<>();
        Cursor cursor = ShiningFingerLite.getWritableDatabase().rawQuery("select * from Student", null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            //根据列的索引直接读取  比如第0列的值
            String p = cursor.getString(3);
            //                            data.addFirst(p);
            data.add(0, p);
            //                            将最新的数据放到第一条
        }
        for (int i = 0; i < 3; i++) {
            int n = i + 1;
            data.add(String.valueOf("测试数据:" + n));
        }

        final MyListView listView = (MyListView) view.findViewById(R.id.listView);
        adapter = new BaseAdapter() {
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_raw, null);
                ImageView list_image = convertView.findViewById(R.id.list_image);
                ImageView list_click = convertView.findViewById(R.id.list_click);
                TextView textView = (TextView) convertView.findViewById(R.id.list_title);
                textView.setText(data.get(position));
                return convertView;
            }

            public long getItemId(int position) {
                return position;
            }

            public Object getItem(int position) {
                return data.get(position);
            }

            public int getCount() {
                return data.size();
            }
        };
        listView.setAdapter(adapter);

        listView.setonRefreshListener(new MyListView.OnRefreshListener() {
            public int i = 0;

            public void onRefresh() {

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Cursor cursor = ShiningFingerLite.getWritableDatabase().rawQuery("select * from Student", null);
//                        SharedPreferences.Editor edit = sp.edit();

                        cursor.moveToFirst();
                        int count = 0;
                        int i;
                        data.clear();
                        cursor.moveToFirst();
                        while (cursor.moveToNext()) {
                            //根据列的索引直接读取  比如第0列的值
                            String p = cursor.getString(1);
                            //                            data.addFirst(p);
                            data.add(0, p);
                            //                            将最新的数据放到第一条
                            count++;
                        }
//                        edit.putInt("Countmessage", count);
//                        edit.commit();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }


                }.execute((Void[]) null);
            }
        });
        return view;
    }


    protected int getLayoutRes() {
        return R.layout.activity_newsrecommd;
    }

}
