package com.zx.easyshop.user.login;

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

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    protected Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void login(String userName, String pwd, String url) {
        getView().showPrb();
        call = EasyShopClient.getInstance().registerOrLogin(userName, pwd, url);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult result = new Gson().fromJson(body, UserResult.class);
                if (result.getCode() == 1) {
                    User user = result.getData();
                    //将登陆数据保存到本地配置中
                    CachePreferences.setUser(user);
                    getView().loginSuccess();
                    getView().showMsg("登陆成功");
                } else if (result.getCode() == 2) {
                    getView().hidePrb();
                    getView().loginFailed();
                    getView().showMsg(result.getMsg());
                } else {
                    getView().hidePrb();
                    getView().showMsg("未知错误");
                }
            }
        });
    }
}
