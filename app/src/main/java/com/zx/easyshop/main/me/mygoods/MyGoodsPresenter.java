package com.zx.easyshop.main.me.mygoods;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.main.shop.ShopView;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.GoodsDetail;
import com.zx.easyshop.model.GoodsInfo;
import com.zx.easyshop.model.GoodsResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class MyGoodsPresenter extends MvpNullObjectBasePresenter<ShopView> {
    protected Call call;
    protected int pageInt = 1;//默认界面为第一页，每刷新或加载数据后，改变pageInt的值

    /**
     * 刷新数据
     *
     * @param type
     */
    public void refreshData(String type) {
        getView().showRefresh();
        call = EasyShopClient.getInstance().getMyGoods(1, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
                getView().showRefreshEnd();
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult result = new Gson().fromJson(body, GoodsResult.class);
                switch (result.getCode()) {
                    case 1:
                        List<GoodsInfo> datas = result.getDatas();
                        if (datas.size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(datas);
                            getView().hideRefresh();
                        }
                        pageInt = 2;//分页
                        break;
                    default:
                        getView().showRefreshError(result.getMsg());
                        break;
                }
            }
        });
    }

    /**
     * 加载数据
     */
    public void loadMoreData(String type) {
        getView().showLoadMore();
        call = EasyShopClient.getInstance().getMyGoods(pageInt, type, CachePreferences.getUser().getName());
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
                        getView().showLoadMoreError(result.getMsg());
                        break;
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
