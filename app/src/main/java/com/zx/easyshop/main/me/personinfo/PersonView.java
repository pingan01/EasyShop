package com.zx.easyshop.main.me.personinfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public interface PersonView extends MvpView {

    void showPgb();

    void hidePgb();

    void showMsg(String msg);

    //用来更新头像
    void updataAvatar(String url);
}
