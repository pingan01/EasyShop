package com.zx.easyshop.main.shop.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zx.easyshop.R;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_info);
    }
}
