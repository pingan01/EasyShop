package com.zx.easyshop.main.shop.detail;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.model.GoodsDetail;
import com.zx.easyshop.model.GoodsDetailResult;
import com.zx.easyshop.model.GoodsResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class GoodsDetailPresenter extends MvpNullObjectBasePresenter<GoodsDetailView> {

    protected Call call;
    //删除商品
    protected Call deleteCall;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //获取商品的详细数据
    public void getData(String uuid) {
        getView().showProgress();
        call = EasyShopClient.getInstance().getGoodsData(uuid);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult result = new Gson().fromJson(body, GoodsDetailResult.class);
                if (result.getCode() == 1) {
                    //商品详情
                    GoodsDetail goodsDetail = result.getDatas();
                    //用来存放图片路径集合
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for (int i = 0; i < goodsDetail.getPages().size(); i++) {
                        String page = goodsDetail.getPages().get(i).getUri();
                        arrayList.add(page);
                    }
                    getView().setImageData(arrayList);
                    getView().setData(goodsDetail, result.getUser());
                } else {
                    getView().showError();
                }
            }
        });
    }

    //我的页面中删除商品
    public void delete(String uuid) {
        getView().showProgress();
        deleteCall = EasyShopClient.getInstance().deleteGoods(uuid);
        deleteCall.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult result = new Gson().fromJson(body, GoodsDetailResult.class);
                if (result.getCode() == 1) {
                    getView().deleteEnd();
                    getView().showMessage("删除成功");
                } else {
                    getView().showMessage("删除失败");
                }
            }
        });
    }
}
