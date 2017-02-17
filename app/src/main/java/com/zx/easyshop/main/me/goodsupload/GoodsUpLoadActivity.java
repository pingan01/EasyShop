package com.zx.easyshop.main.me.goodsupload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoodsUpLoadActivity extends MvpActivity<GoodsUpLoadView, GoodsUpLoadPresenter> implements GoodsUpLoadView {

    @BindView(R.id.tv_goods_delete)
    TextView tvGoodsDelete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_goods_name)
    EditText etGoodsName;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.btn_goods_type)
    Button btnGoodsType;
    @BindView(R.id.et_goods_describe)
    EditText etGoodsDescribe;
    @BindView(R.id.btn_goods_load)
    Button btnGoodsLoad;

    protected ActivityUtils activityUtils;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //左上角返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public GoodsUpLoadPresenter createPresenter() {
        return new GoodsUpLoadPresenter();
    }

    //*******************************接口视图相关实现
    @Override
    public void showPgb() {

    }

    @Override
    public void hidePgb() {

    }

    @Override
    public void upLoadSuccess() {

    }

    @Override
    public void showMsg(String msg) {

    }
}
