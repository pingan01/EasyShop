package com.zx.easyshop.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.main.me.MeFragment;
import com.zx.easyshop.main.shop.ShopFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindViews({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] textViews;
    @BindView(R.id.main_toobar)
    Toolbar toolbar;
    @BindView(R.id.main_title)
    TextView tv_title;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    protected Unbinder unbinder;
    protected ActivityUtils activityUtils;
    protected boolean isExit = false;//点击两次返回，退出程序，默认为不退
    protected int position;//点击的TextView所在的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        init();
    }

    /**
     * 初始化视图
     */
    public void init() {
        //将数据源加载到视图中
        viewPager.setAdapter(unLoginAdapter);

        //刚进来默认选中市场
        textViews[0].setSelected(true);//xml文件中指定选择器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动
            }

            @Override
            public void onPageSelected(int position) {
                //所有的TextView都未选中
                for (TextView textView : textViews) {
                    textView.setSelected(false);
                }
                //更改标题
                tv_title.setText(textViews[position].getText());
                //设置选中
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //用户未登陆时的ViewPager适配器
    protected FragmentStatePagerAdapter unLoginAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                //消息
                case 1:
                    return new UnLoginFragment();
                //通讯录
                case 2:
                    return new UnLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    //textView的点击事件
    @OnClick({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    public void onClick(View view) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);//全部未选中
            textViews[i].setTag(i);
        }
        //给当前View设置选择效果
        view.setSelected(true);
        //切换，不需要平滑效果，直接进行切换（False）
        viewPager.setCurrentItem((int) view.getTag(), false);
        //设置toolbar的标题
        tv_title.setText(textViews[(int) view.getTag()].getText());
    }

    //点击两次返回，退出程序，默认为不退
    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;//第一次确认退出
            activityUtils.showToast("再按一次退出程序");
            //在两秒内再次点击返回则退出

            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;//2s内不退出
                }
            }, 2000);
        } else {
            finish();//2s内退出，销毁当前界面
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();//解绑Butterknife
    }
}
