package com.zx.easyshop.main.me.goodsupload;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.zx.easyshop.commons.MyFileUtils;
import com.zx.easyshop.model.GoodsUpLoad;
import com.zx.easyshop.model.ImageItem;
import com.zx.easyshop.network.EasyShopClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class GoodsUpLoadPresenter extends MvpNullObjectBasePresenter<GoodsUpLoadView> {

    protected Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call == null) call.cancel();
    }

    /**
     * @param goodsUpLoad
     * @param list：图片的路径
     */
    public void upLoad(GoodsUpLoad goodsUpLoad, List<ImageItem> list) {
        getView().showPgb();
        call = EasyShopClient.getInstance().upLoadGoods(goodsUpLoad, getFiles(list));
    }

    //根据imageItem（图片路径）获取图片文件
    private ArrayList<File> getFiles(List<ImageItem> list) {
        ArrayList<File> files = new ArrayList<>();
        for (ImageItem imageItem : list
                ) {
            //根据图片路径来获取图片
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }
}
