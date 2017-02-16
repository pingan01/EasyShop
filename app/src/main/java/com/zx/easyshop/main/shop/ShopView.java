package com.zx.easyshop.main.shop;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.zx.easyshop.model.GoodsInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public interface ShopView extends MvpView{

    //刷新--处理中--
    void showRefresh();

    //刷新--失败---
    void showRefreshError(String msg);

    //刷新--结束--
    void showRefreshEnd();

    //刷新--隐藏刷新视图
    void hideRefresh();

    //加载--加载中--
    void showLoadMore();

    //加载--加载失败--
    void showLoadMoreError(String msg);

    //加载--加载结束--
    void showLoadMoreEnd();

    //加载--隐藏加载视图
    void hideLoadMore();

    //添加数据--刷新
    void addRefreshData(List<GoodsInfo> goodsInfos);

    //添加数据--加载
    void addLoadMoreData(List<GoodsInfo> goodsInfos);

    //显示消息
    void showMessage(String msg);

}
