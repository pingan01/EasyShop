package com.zx.easyshop.network;

import android.graphics.Canvas;
import android.view.View;

import com.google.gson.Gson;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    protected Gson gson;

    private EasyShopClient() {
        //搭建客户端
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        gson = new Gson();
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
     * 注册和登陆
     *
     * @param userName
     * @param pwd
     * @param url
     */
    public Call registerOrLogin(String userName, String pwd, String url) {
        //请求体：表单形式
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

    /**
     * 修改昵称
     *
     * @param user
     * @return
     */
    public Call uploadUser(User user) {
        //构建请求体：多部分形式
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //传一个用户实体类，转换为json字符串（Gson）
                .addFormDataPart("user", gson.toJson(user))
                .build();
        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 更新图片
     *
     * @param file
     */
    public Call updataAvatar(File file) {
        //构建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", gson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                .build();
        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    //获取所有商品
    public Call getGoods(int pageNo, String type) {
        //构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("type", type)
                .build();
        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    //获取商品详情
    public Call getGoodsData(String goodsUuid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid", goodsUuid)
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.DETAIL)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }
}
