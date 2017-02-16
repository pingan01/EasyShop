package com.zx.easyshop.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

/**
 * 方法内代码能在主线程中运行的CallBack
 */
public abstract class UICallback implements Callback {
    //拿到主线程的Handler
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        //发送一个在主线程中运行的RUN方法
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call, e);
            }
        });
    }
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        //判断是否响应成功
        if (!response.isSuccessful()) {
            throw new IOException("错误异常" + response.code());
        }
        //拿到JSON字符串
        final String body = response.body().string();
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call, body);
            }
        });
    }

    //在主线程中运行此方法：为抽象方法，具体执行内容在调用时去实现
    public abstract void onFailureUI(Call call, IOException e);

    public abstract void onResponseUI(Call call, String body);
}
