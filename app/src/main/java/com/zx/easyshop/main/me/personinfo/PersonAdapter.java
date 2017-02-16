package com.zx.easyshop.main.me.personinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zx.easyshop.R;
import com.zx.easyshop.model.ItemShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class PersonAdapter extends BaseAdapter {

    protected List<ItemShow> mList = new ArrayList<>();
    protected Context mContext;

    public PersonAdapter(List<ItemShow> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_person_info, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item_name.setText(mList.get(position).getItem_title());
        holder.tv_person.setText(mList.get(position).getItem_content());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_person)
        TextView tv_person;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
