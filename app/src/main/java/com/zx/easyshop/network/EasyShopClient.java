package com.zx.easyshop.network;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/2/9 0009.
 * <p>
 * 易淘客户端创建
 */

public class EasyShopClient {
    //单例模式：客户端允许只有一个
    public static EasyShopClient mEasyShopClient;
    protected OkHttpClient okHttpClient;

    private EasyShopClient() {
        //搭建客户端
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public static EasyShopClient getInstance() {
        if (mEasyShopClient == null) {
            synchronized (EasyShopClient.class) {
                if (mEasyShopClient == null)
                    mEasyShopClient = new EasyShopClient();
            }
        }
        return mEasyShopClient;
    }

    /**
     * @param userName
     * @param pwd
     * @param url
     */
    public Call registerOrLogin(String userName, String pwd, String url) {
        //请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", pwd)
                .build();
        //创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求
        return okHttpClient.newCall(request);
    }

}
