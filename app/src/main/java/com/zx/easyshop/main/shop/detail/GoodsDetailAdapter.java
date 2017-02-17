package com.zx.easyshop.main.shop.detail;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zx.easyshop.main.shop.ShopAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class GoodsDetailAdapter extends PagerAdapter {
    protected ArrayList<ImageView> imageViews;//存放图片的集合，数据源

    public GoodsDetailAdapter(ArrayList<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    //实例化Item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) onItemClickListener.onItemClick();
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return imageViews != null ? imageViews.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //销毁子视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //*****************ViewPager 的Item的单击事件
    public interface OnItemClickListener {
        void onItemClick();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
