package com.zx.easyshop.main.me.personinfo;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.User;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class PersonPresenter extends MvpNullObjectBasePresenter<PersonView> {

    protected Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //上传头像
    public void updataAvatar(File file) {
        // 上传头像，执行网络请求
        getView().showPgb();
        Call call = EasyShopClient.getInstance().updataAvatar(file);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidePgb();//隐藏动画
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {

                UserResult result = new Gson().fromJson(body, UserResult.class);
                if (result == null) {
                    getView().showMsg("未知错误1");
                } else if (result.getCode() != 1) {
                    getView().showMsg(result.getMsg());
                }else{
                    User user = result.getData();
                    CachePreferences.setUser(user);
                    //上传成功、触发UI操作(更换头像)
                    getView().updataAvatar(result.getData().getImage());
                    getView().showMsg("更换头像");
                    getView().hidePgb();
                }


                // TODO: 2017/2/15 0015 环信头像更新 
            }
        });

    }
}
