package com.zx.easyshop.main.me.mygoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.main.shop.ShopAdapter;
import com.zx.easyshop.main.shop.ShopView;
import com.zx.easyshop.main.shop.detail.GoodsDetailActivity;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.GoodsInfo;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyGoodsActivity extends MvpActivity<ShopView, MyGoodsPresenter> implements ShopView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout mRefreshLayout;
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    @BindView(R.id.tv_load_empty)
    TextView tvLoadEmpty;
    @BindString(R.string.load_more_end)

    String load_more_end;//没有更多的数据

    protected ActivityUtils activityUtils;
    protected ShopAdapter mAdapter;
    protected Unbinder unbinder;
    //获取商品时，商品类型，获取全部商品时为空
    protected String pageType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        mAdapter = new ShopAdapter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置toolbar的监听事件
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        init();//视图初始化
    }

    //点击左上角返回，finish
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        //初始化RecycleView
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                //跳转到我的商品详情页面
                Intent intent = GoodsDetailActivity.getStartIntent(MyGoodsActivity.this, goodsInfo.getGoodsTable_ID(), 1);
                //从我的商品页面跳转，状态为1
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        //初始化RefreshLayout
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        //关闭Header所需时间
        mRefreshLayout.setDurationToClose(1500);
        mRefreshLayout.setBackgroundResource(R.color.recycler_bg);
        //加载回调
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //加载更多
                presenter.loadMoreData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //开始刷新
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //当前页面没数据
        if (mAdapter.getItemCount() == 0) {
            mRefreshLayout.autoRefresh();//自动刷新
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //  ####################### toolbar 菜单选项相关   #########################
    //创建右上角的选择菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_goods_type, menu);
        return true;
    }

    //toolbar菜单对应的点击事件
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };

    @NonNull
    @Override
    public MyGoodsPresenter createPresenter() {
        return new MyGoodsPresenter();
    }

    //**********************************接口视图的相关实现
    @Override
    public void showRefresh() {
        tvLoadEmpty.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        mRefreshLayout.refreshComplete();
        if (mAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        mRefreshLayout.refreshComplete();
        tvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefresh() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMore() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        mRefreshLayout.refreshComplete();
        if (mAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(load_more_end);
    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> goodsInfos) {
        mAdapter.clear();
        if (goodsInfos != null) mAdapter.addData(goodsInfos);
    }

    @Override
    public void addLoadMoreData(List<GoodsInfo> goodsInfos) {
        mAdapter.addData(goodsInfos);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
