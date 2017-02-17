package com.zx.easyshop.main.me.goodsupload;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public interface GoodsUpLoadView extends MvpView {

    void showPgb();

    void hidePgb();

    void upLoadSuccess();

    void showMsg(String msg);


}
