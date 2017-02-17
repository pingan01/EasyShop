package com.zx.easyshop.main.me.goodsupload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.easyshop.model.ImageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class GoodsUpLoadAdapter extends RecyclerView.Adapter {

    protected LayoutInflater inflater;
    protected ArrayList<ImageItem> list = new ArrayList<>();

    public GoodsUpLoadAdapter(Context context, ArrayList<ImageItem> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    //***********************************模式的选择-------开始
    //图片编辑时模式有两种：有图与无图---有图时，长按图片在图片右上角显示CheckBox,同时右上角显示“删除”文本
    //                     无图时，显示“+”按钮
    public static final int MODE_NORMAL = 1;//有图
    public static final int MODE_MULTI_SELECT = 2;//无图

    //代表图片编辑的模式
    public int mode;

    //用枚举，表示item的类型，有图或者无图
    public enum ITEM_TYPE {
        ITEM_NORMAL, ITEM_ADD
    }

    //改变模式
    public void changeModel(int mode) {
        this.mode = mode;
        notifyDataSetChanged();

    }

    //获取当前模式
    public int getModel() {
        return mode;
    }

    //*********************************逻辑的选择------------结束

    //**********************************外部调用的方法----------开始

    //添加图片
    public void addImage(ImageItem imageItem) {
        list.add(imageItem);
    }

    //获取图片的个数
    public int getSize() {
        return list != null ? list.size() : 0;
    }

    //获取图片数据的集合
    public ArrayList<ImageItem> getList() {
        return list;
    }

    //刷新数据
    public void notifyData() {
        notifyDataSetChanged();
    }

    //**************************************外部调用方法------结束
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //添加图片的ViewHolder
    public static class ItemAddViewHolder extends RecyclerView.ViewHolder {

        public ItemAddViewHolder(View itemView) {
            super(itemView);
        }
    }

    //已经有图片的ViewHolder
    public static class ItemSelectViewHolder extends RecyclerView.ViewHolder {

        public ItemSelectViewHolder(View itemView) {
            super(itemView);
        }
    }
}
