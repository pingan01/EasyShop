package com.zx.easyshop.user.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public interface RegisterView extends MvpView{
    //显示动画
    void showPgb();

    //隐藏动画
    void hidePgb();

    //注册成功
    void registerSuccess();

    //注册失败
    void registerFailed();

    //提示消息
    void showMsg(String msg);

    //用户名密码不正确时，提示用户
    void showUserPassWordError(String msg);

}
