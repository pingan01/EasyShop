package com.zx.easyshop.main.shop;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.model.GoodsInfo;
import com.zx.easyshop.model.GoodsResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView> {

    protected Call call;
    protected int pageInt = 1;//默认界面为第一页，每刷新或加载数据后，改变pageInt的值

    //业务：刷新数据/加载数据
    public void refreshData(String type) {
        getView().showRefresh();
        //刷新数据，永远是最新数据，也就说永远请求第一页
        call = EasyShopClient.getInstance().getGoods(pageInt, type);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult result = new Gson().fromJson(body, GoodsResult.class);
                switch (result.getCode()) {
                    case 1:
                        //服务器没有商品的时候(没有数据)
                        List<GoodsInfo> datas = result.getDatas();
                        if (datas.size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(datas);
                            getView().showRefreshEnd();
                        }
                        //分页为2，加载更多数据
                        pageInt = 2;
                        break;
                    default:
                        //网络连接失败的时候
                        getView().showRefreshError(result.getMsg());

                }
            }
        });
    }

    public void loadData(String type) {
        getView().showLoadMore();
        call = EasyShopClient.getInstance().getGoods(pageInt, type);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
                getView().showLoadMoreEnd();
            }
            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult result = new Gson().fromJson(body, GoodsResult.class);
                switch (result.getCode()) {
                    case 1:
                        List<GoodsInfo> datas = result.getDatas();
                        if (datas.size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
                            getView().addLoadMoreData(datas);
                            getView().showLoadMoreEnd();
                        }
                        pageInt++;
                        break;
                    default:
                        //网络连接失败的时候
                        getView().showLoadMoreError(result.getMsg());
                }
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call == null) call.cancel();
    }
}
