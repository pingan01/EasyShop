package com.zx.easyshop.main.shop.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.easyshop.R;
import com.zx.easyshop.components.AvatarLoadOptions;
import com.zx.easyshop.network.EasyShopApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 商品图片展示的详情页面
 */
public class GoodsDetailInfoActivity extends AppCompatActivity {

    //用来拿图片地址的key
    private static final String IMAGES = "images";

    public static Intent getStartIntent(Context context, ArrayList<String> imgUrl) {
        Intent intent = new Intent(context, GoodsDetailInfoActivity.class);
        intent.putExtra(IMAGES, imgUrl);
        return intent;
    }

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    protected ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_info);
        list = new ArrayList<>();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        list = getIntent().getStringArrayListExtra(IMAGES);
        //用商品详情页的适配器
        GoodsDetailAdapter adapter = new GoodsDetailAdapter(getImage(list));
        viewPager.setAdapter(adapter);
        adapter.setOnItemClickListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                finish();
            }
        });
        indicator.setViewPager(viewPager);
    }

    private ArrayList<ImageView> getImage(ArrayList<String> list) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL + list.get(i),
                    imageView, AvatarLoadOptions.build_item());
            imageViews.add(imageView);
        }
        return imageViews;
    }
}
