package com.zx.easyshop.main.shop.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.zx.easyshop.model.GoodsDetail;
import com.zx.easyshop.model.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public interface GoodsDetailView extends MvpView {

    void showProgress();

    void hideProgress();

    //设置图片路径
    void setImageData(ArrayList<String> arrayList);

    //设置商品信息
    void setData(GoodsDetail data, User goods_user);

    //*商品不存在了*/
    void showError();

    //提示信息
    void showMessage(String msg);

    //*删除商品*/
    void deleteEnd();
}
