package com.zx.easyshop.user.register;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.User;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    protected Call call;

    //业务:执行网络请求
    //特定的地方，触发对UI进行操作

    //对视图进行初始化：在操作业务的时候，先调用此方法
    @Override
    public void attachView(RegisterView view) {
        super.attachView(view);
    }

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void register(String userName, String pwd, String url) {
        //显示加载动画
        getView().showPgb();
        call = EasyShopClient.getInstance().registerOrLogin(userName, pwd, url);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                //隐藏动画
                getView().hidePgb();
                //展示错误信息
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                //隐藏动画
                getView().hidePgb();
                //拿到返回结果（gson）
                UserResult result = new Gson().fromJson(body, UserResult.class);
                if (result.getCode() == 1) {
                    getView().showMsg("注册成功");
                    User user = result.getData();
                    //将用户信息保存到本地配置中
                    CachePreferences.setUser(user);
                    //执行注册成功的方法
                    getView().registerSuccess();
                } else if (result.getCode() == 2) {
                    //提示失败信息
                    getView().showMsg(result.getMsg());
                    //执行注册失败的方法
                    getView().registerFailed();
                } else {
                    getView().showMsg("未知错误！");
                }

            }
        });

    }

}
