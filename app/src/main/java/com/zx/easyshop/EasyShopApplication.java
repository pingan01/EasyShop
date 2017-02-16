package com.zx.easyshop;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zx.easyshop.model.CachePreferences;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class EasyShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CachePreferences.init(this);
        //##########################  imageLoader的初始化      ######################

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)//开启硬盘缓存
                .cacheInMemory(true)//开启内存缓存
                .resetViewBeforeLoading(true)//加载前重置Imageview
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(4 * 1024 * 1024)//设置内存缓存的大小
                .defaultDisplayImageOptions(displayImageOptions)//设置默认的加载选项
                .build();

        ImageLoader.getInstance().init(configuration);
    }
}