package com.zx.easyshop.user.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.commons.LogUtils;
import com.zx.easyshop.components.ProgressDialogFragment;
import com.zx.easyshop.main.MainActivity;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopApi;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;
import com.zx.easyshop.user.register.RegisterActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected ActivityUtils activityUtils;
    protected ProgressDialogFragment dialogFragment;
    protected Unbinder unbinder;
    protected String userName;
    protected String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        init();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    //实现菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();//如果点击的是返回按钮，则退出登陆界面
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        //添加一个返回按钮，重写菜单的点击事件，点击后退出登陆界面
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回按钮

        //给EditText添加监听事件
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = et_username.getText().toString();
                pwd = et_pwd.getText().toString();
                //是否为空
                boolean login = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd));
                btn_login.setEnabled(login);//只要EditText两者不为空就可以登录
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            //这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。
            //而after表示改变后新的内容的数量
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。
            //而before表示被改变的内容的数量。
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //表示最终的状态
            @Override
            public void afterTextChanged(Editable s) {
                userName = et_username.getText().toString();
                pwd = et_pwd.getText().toString();
                //是否为空
                boolean login = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd));
                btn_login.setEnabled(login);//只要EditText两者不为空就可以登录
            }
        });

    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onClick(View view) {
        if (view.getId() == tv_register.getId()) {
            activityUtils.startActivity(RegisterActivity.class);
        } else if (view.getId() == btn_login.getId()) {
            //调用业务类的登陆方法
            presenter.login(userName, pwd, EasyShopApi.BASE_URL + EasyShopApi.LOGIN);
            activityUtils.showToast("点击了登陆按钮");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //###############################  视图接口相关 ###############

    @Override
    public void showPrb() {
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "progress_login_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void loginFailed() {
        et_username.setText("");
    }

    @Override
    public void loginSuccess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }
}