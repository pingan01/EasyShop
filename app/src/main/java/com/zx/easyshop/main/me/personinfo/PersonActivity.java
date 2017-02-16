package com.zx.easyshop.main.me.personinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.components.AvatarLoadOptions;
import com.zx.easyshop.components.PicWindow;
import com.zx.easyshop.components.ProgressDialogFragment;
import com.zx.easyshop.main.MainActivity;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.ItemShow;
import com.zx.easyshop.model.User;
import com.zx.easyshop.network.EasyShopApi;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends MvpActivity<PersonView, PersonPresenter> implements PersonView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;//用户头像
    @BindView(R.id.listView)
    ListView listView;//显示用户名，昵称，环信ID的listView

    protected ActivityUtils activityUtils;
    protected List<ItemShow> list;//数据源
    protected PersonAdapter adapter;
    protected ProgressDialogFragment progressDialogFragment;
    protected PicWindow picWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        adapter = new PersonAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        activityUtils.showToast("用户名作为登录名不能更改");
                        break;
                    case 1:
                        //昵称界面
                        activityUtils.startActivity(NickNameActivity.class);
                        break;
                    case 2:
                        activityUtils.showToast("环信ID不可以更改");
                        break;
                }
            }
        });
        //获取用户头像
        updataAvatar(CachePreferences.getUser().getImage());
    }

    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    /**
     * 改完昵称后，需要更改数据
     */
    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }
    /**
     * 数据初始化
     */
    public void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow("用户名", user.getName()));
        list.add(new ItemShow("昵称", user.getNickname()));
        list.add(new ItemShow("环信ID", user.getHx_ID()));
    }


    //显示返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);

    }

    @OnClick({R.id.iv_user_head, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击头像
            case R.id.iv_user_head:
                //头像来源选择（相册，拍照）
                //如果为空，创建实例（实现监听）
                if (picWindow == null) {
                    picWindow = new PicWindow(this, new PicWindow.Listener() {
                        //图片选择弹窗的自定义监听
                        @Override
                        public void toGallery() {
                            //在相册中选取
                            //清空裁剪的缓存
                            CropHelper.clearCachedCropFile(handler.getCropParams().uri);
                            //跳转到相册
                            Intent intent = CropHelper.buildCropFromGalleryIntent(handler.getCropParams());
                            startActivityForResult(intent, CropHelper.REQUEST_CROP);
                        }

                        @Override
                        public void toCamera() {
                            //在相机中选取
                            CropHelper.clearCachedCropFile(handler.getCropParams().uri);
                            Intent intent = CropHelper.buildCaptureIntent(handler.getCropParams().uri);
                            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                        }
                    });
                }
                //如果已经显示
                if (picWindow.isShowing()) {
                    picWindow.dismiss();
                    return;
                }
                picWindow.show();//展示图片来源库
                break;
            //退出登陆
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                //清除所有旧的Activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // TODO: 2017/2/14 0014 退出环信相关
        }
    }

    //图片处理裁剪的handler
    protected CropHandler handler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //通过uri拿到图片文件
            File file = new File(uri.getPath());
            // 业务类上传头像
            presenter.updataAvatar(file);
        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {

        }

        @Override
        public CropParams getCropParams() {
            //自定义裁剪参数的大小
            CropParams params = new CropParams();
            params.aspectX = 500;
            params.aspectY = 500;
            return params;
        }

        @Override
        public Activity getContext() {
            return PersonActivity.this;//返回Activity
        }
    };

    //实现Activity中的方法（裁剪完之后）
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理裁剪完的头像
        CropHelper.handleResult(handler, requestCode, resultCode, data);
        activityUtils.showToast("裁剪成功，准备更新");
    }
    // ######################    视图接口相关    #####################

    @Override
    public void showPgb() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(), "progress_person_dialog_fragment");
    }

    @Override
    public void hidePgb() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    //头像更新
    @Override
    public void updataAvatar(String url) {
        // 头像更新加载
        //参数：1、头像的路径(服务器的)   2、头像显示的控件     3、加载的选项
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL + url, ivUserHead,
                AvatarLoadOptions.build());
    }
}
