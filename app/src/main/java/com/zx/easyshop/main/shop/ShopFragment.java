package com.zx.easyshop.main.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.model.GoodsInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/2/7 0007.
 * <p>
 * 市场页面
 */

public class ShopFragment extends MvpFragment<ShopView, ShopPresenter> implements ShopView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;//展示商品的列表
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout mRefreshLayout;//刷新加载的控件
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;//错误提示

    protected ActivityUtils activityUtils;
    protected ShopAdapter shopAdapter;

    //获取商品时，商品类型，获取全部商品时为空
    protected String pageType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        shopAdapter = new ShopAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //给adapter添加跳转监听事件
        shopAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                activityUtils.showToast("跳转到详情页,待实现");
                // TODO: 2017/2/16 0016
            }
        });
        mRecyclerView.setAdapter(shopAdapter);
        /**
         * 初始化RefreshLayout
         */
        //使用本对象作为key，用来记录上一次刷新的事件，如果两次下拉刷新间隔太近，不会触发刷新方法
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        //设置刷新时显示的背景色
        mRefreshLayout.setBackgroundResource(R.color.recycler_bg);
        //关闭Header所耗时间
        mRefreshLayout.setDurationToCloseHeader(1500);
        //实现刷新，加载回调
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            //加载更多
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            //开始刷新
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //当前页面没数据
        if (shopAdapter.getItemCount() == 0) {
            mRefreshLayout.autoRefresh();//自动刷新
        }
    }

    //点击错误视图时刷数据
    @OnClick(R.id.tv_load_error)
    public void onClick() {
        //自动刷新
        mRefreshLayout.autoRefresh();
    }
    //****************************  视图接口的相关实现

    @Override
    public void showRefresh() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        //停止刷新
        mRefreshLayout.refreshComplete();
        //判断是否拿到数据
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast("没有新的商品了");
        //停止刷新
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        //停止刷新
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMore() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        //停止刷新
        mRefreshLayout.refreshComplete();
        //判断是否拿到数据
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast("没有更多数据");
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> goodsInfos) {
        //数据清空
        shopAdapter.clear();
        if (goodsInfos != null) {
            shopAdapter.addData(goodsInfos);
        }
    }

    @Override
    public void addLoadMoreData(List<GoodsInfo> goodsInfos) {
        shopAdapter.addData(goodsInfos);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
