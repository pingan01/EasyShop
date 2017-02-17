package com.zx.easyshop.main.shop.detail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.components.AvatarLoadOptions;
import com.zx.easyshop.components.ProgressDialogFragment;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.GoodsDetail;
import com.zx.easyshop.model.User;
import com.zx.easyshop.network.EasyShopApi;
import com.zx.easyshop.user.login.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends MvpActivity<GoodsDetailView, GoodsDetailPresenter> implements GoodsDetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    /*使用开源库CircleIndicator来实现ViewPager的圆点指示器。*/
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_detail_name)
    TextView tv_detail_name;
    @BindView(R.id.tv_detail_price)
    TextView tv_detail_price;
    @BindView(R.id.tv_detail_master)
    TextView tv_detail_master;
    @BindView(R.id.tv_detail_describe)
    TextView tv_detail_describe;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.tv_goods_error)
    TextView tv_goods_error;
    @BindView(R.id.btn_detail_message)
    Button btn_detail_message;

    private String str_uuid;
    private ArrayList<ImageView> imageViews;//存放图片的集合
    private ArrayList<String> list_uri;//存放图片路径的集合
    private GoodsDetailAdapter adapter;//viewPager的适配器
    private ActivityUtils activityUtils;
    private User goods_user;
    private ProgressDialogFragment progressDialogFragment;
    private static final String UUID = "uuid";
    //从不同的页面进入详情页面的状态值  0为从市场页面   1为从我的商品页面
    private static final String STATE = "state";

    /**
     * 从不同的页面跳转到商品详情页面
     *
     * @param context
     * @param uuid
     * @param state
     * @return
     */
    public static Intent getStartIntent(Context context, String uuid, int state) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(UUID, uuid);
        intent.putExtra(STATE, state);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

        //增加左上角的返回图片
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViews = new ArrayList<>();
        list_uri = new ArrayList<>();
        adapter = new GoodsDetailAdapter(imageViews);

        adapter.setOnItemClickListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //点击图片，跳转到图片详情页面
                Intent intent = GoodsDetailInfoActivity.getStartIntent(GoodsDetailActivity.this, list_uri);
                startActivity(intent);
            }
        });
        viewPager.setAdapter(adapter);
        init();//初始化视图
    }

    private void init() {
        //拿到商品主键（uuid）
        str_uuid = getIntent().getStringExtra(UUID);
        //来自哪个页面：  0--市场页面   1--我的商品页面
        int btn_show = getIntent().getIntExtra(STATE, 0);//默认值为从市场页面跳转
        //如果来自我的商品页面
        if (btn_show == 1) {
            tv_goods_delete.setVisibility(View.VISIBLE);//显示“删除”文本
            btn_detail_message.setVisibility(View.GONE);//隐藏“发消息”按钮(自己不给自己发消息)
        }
        //获取商品详情业务
        presenter.getData(str_uuid);
    }

    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }

    //返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    //点击事件：发消息/删除
    @OnClick({R.id.tv_goods_delete, R.id.btn_detail_message})
    public void onClick(View view) {
        //判断登陆状态
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_goods_delete://删除
                activityUtils.showToast("删除相关功能，待实现");
                // TODO: 2017/2/16 0016 删除相关功能 
                break;
            case R.id.btn_detail_message://发消息
                activityUtils.showToast("环信发消息功能,待实现");
                break;
            // TODO: 2017/2/16 0016 跳转到环信发消息界面
        }
    }

    //****************************************视图相关事件实现*********************
    @Override
    public void showProgress() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(), "progress_goodsDetail_dialog_fragment");
    }

    @Override
    public void hideProgress() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void setImageData(ArrayList<String> arrayList) {
        list_uri = arrayList;
        for (int i = 0; i < list_uri.size(); i++) {
            ImageView imageView = new ImageView(this);
            //加载图片:第三方库
            ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL + list_uri.get(i),
                    imageView, AvatarLoadOptions.build_item());
            //添加到储存图片的集合中
            imageViews.add(imageView);
        }
        adapter.notifyDataSetChanged();
        //确认图片数量，创建图片圆点指示器
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setData(GoodsDetail data, User goods_user) {
        //展示数据
        this.goods_user = goods_user;
        tv_detail_name.setText(data.getName());
        tv_detail_price.setText(getString(R.string.goods_money, data.getPrice()));
        tv_detail_master.setText(getString(R.string.goods_detail_master, goods_user.getNickname()));
        tv_detail_describe.setText(data.getDescription());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void deleteEnd() {

    }
}
