package com.zx.easyshop.main.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.easyshop.R;
import com.zx.easyshop.components.AvatarLoadOptions;
import com.zx.easyshop.model.GoodsInfo;
import com.zx.easyshop.network.EasyShopApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    //所需数据
    private List<GoodsInfo> datas = new ArrayList<>();
    private Context context;

    //添加数据
    public void addData(List<GoodsInfo> data) {
        datas.addAll(data);
        //通知更新
        notifyDataSetChanged();
    }

    //清空数据
    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        ShopViewHolder viewHolder = new ShopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        //商品名称
        holder.tv_name.setText(datas.get(position).getGoodsName());
        //商品价格,替换字符串
        String price = context.getString(R.string.goods_money, datas.get(position).getPrice());
        holder.tv_price.setText(price);
        //商品图片，图片加载
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL + datas.get(position).getGoodsImage(), holder.imageView,
                AvatarLoadOptions.build_item());

        //点击图片，跳转到商品详情界面
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClicked(datas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_recycler)
        ImageView imageView;//商品图片
        @BindView(R.id.tv_item_name)
        TextView tv_name;//商品名
        @BindView(R.id.tv_item_price)
        TextView tv_price;//商品价格

        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //#####################  item点击事件（接口回调）
    public interface OnItemClickListener {
        //点击item商品，传递商品信息
        void onItemClicked(GoodsInfo goodsInfo);
    }

    private OnItemClickListener onItemClickListener;

    //对外提供监听方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
