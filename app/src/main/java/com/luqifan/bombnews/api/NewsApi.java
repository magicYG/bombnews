package com.luqifan.bombnews.api;

import com.luqifan.bombnews.bean.News;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by 陆启凡 on 2017/9/5.
 */

public interface NewsApi {
//    http://c.m.163.com/nc/article/list/T1350383429665/0-10.html
//    http://c.m.163.com/nc/article/headline/T1348647909107/0-10.html
//    http://c.m.163.com/nc/article/list/T1348649145984/0-10.html
//    http://c.m.163.com/nc/article/list/T1348654060988/0-10.html

    @GET("china")
    Observable<List<News>> getData();
}

