package com.zx.easyshop.main.me.personinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.commons.RegexUtils;
import com.zx.easyshop.model.CachePreferences;
import com.zx.easyshop.model.User;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class NickNameActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_nickname)
    EditText etNickName;

    protected ActivityUtils activityUtils;
    protected String str_nickname;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        unbinder = ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save)
    public void onClick(View view) {
        //获取用户输入的昵称
        str_nickname = etNickName.getText().toString();
        //判断昵称是否符合规则
        if (RegexUtils.verifyNickname(str_nickname) != RegexUtils.VERIFY_SUCCESS) {
            String msg = "昵称为中文，字母或数字，长度为1~12，一个中文算2个长度";
            activityUtils.showToast(msg);
        }
        //修改昵称
        init();
    }

    /**
     * 修改昵称
     */
    public void init() {
        //从本地配置中拿出用户类
        final User user = CachePreferences.getUser();
        //将昵称设置
        user.setNickname(str_nickname);
        //执行修改昵称的网络请求
        Call call = EasyShopClient.getInstance().uploadUser(user);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                activityUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult result = new Gson().fromJson(body, UserResult.class);
                //修改失败,则跳出
                if (result.getCode() != 1) {
                    activityUtils.showToast(result.getMsg());
                    return;
                }
                    //修改成功
                    CachePreferences.setUser(result.getData());
                    activityUtils.showToast("修改成功");
                    //返回
                    onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
