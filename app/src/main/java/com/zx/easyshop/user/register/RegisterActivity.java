package com.zx.easyshop.user.register;

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

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.commons.LogUtils;
import com.zx.easyshop.commons.RegexUtils;
import com.zx.easyshop.components.AlertDialogFragment;
import com.zx.easyshop.components.ProgressDialogFragment;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopApi;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;
import com.zx.easyshop.user.login.LoginActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwdAgain)
    EditText et_pwdAgain;
    @BindView(R.id.btn_register)
    Button btn_register;

    protected ActivityUtils activityUtils;
    protected ProgressDialogFragment dialogFragment;
    protected Unbinder unbinder;
    protected String userName;
    protected String password;
    protected String pwd_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        init();
    }


    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();//创建注册的业务类
    }

    //实现菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();//如果点击是返回按钮，则退出注册界面
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化视图
     */
    public void init() {
        //添加返回按钮，重写菜单的点击事件，点击后退出注册界面
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回按钮

        //给EditText添加监听事件
        et_username.addTextChangedListener(textWatcher);
        et_pwd.addTextChangedListener(textWatcher);
        et_pwdAgain.addTextChangedListener(textWatcher);
    }

    protected TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            userName = et_username.getText().toString();
            password = et_pwd.getText().toString();
            pwd_again = et_pwdAgain.getText().toString();
            boolean register = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password));
            btn_register.setEnabled(register);
        }
    };

    @OnClick(R.id.btn_register)
    public void onClick(View view) {
        if (RegexUtils.verifyUsername(userName) != RegexUtils.VERIFY_SUCCESS) {
            String msg = "账号为中文，字母或数字，长度为4~20，一个中文算2个长度";
            showUserPassWordError(msg);
            return;
        } else if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            String msg = "密码以数字或字母开头，长度在6~18之间，只能包含字符、数字和下划线";
            showUserPassWordError(msg);
            return;
        } else if (!TextUtils.equals(password, pwd_again)) {
            String msg = "两次输入的密码不同！";
            showUserPassWordError(msg);
            return;
        }
        activityUtils.showToast("点击了注册按钮");

        //业务类执行注册的业务
        presenter.register(userName, password, EasyShopApi.BASE_URL + EasyShopApi.REGISTER);

//        //请求体:数据当做JSON格式的
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("username", userName);
//            jsonObject.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String json = jsonObject.toString();
//        RequestBody requestBody = RequestBody.create(null, json);

        //客户端发送请求方式：异步回调
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //##########################################     视图接口的实现    ######################
    @Override
    public void showPgb() {
        //关闭软键盘
        activityUtils.hideSoftKeyboard();
        //初始化进度条
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        //如果进度条已经显示，则跳出
        if (dialogFragment.isVisible()) return;
        //进度条显示
        dialogFragment.show(getSupportFragmentManager(), "progress_register_dialog_fragment");
    }

    @Override
    public void hidePgb() {
        dialogFragment.dismiss();
    }

    @Override
    public void registerSuccess() {
        //跳转：
        activityUtils.startActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void registerFailed() {
        et_username.setText("");
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showUserPassWordError(String msg) {
        //弹出对话框，提示错误信息
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(msg);
        fragment.show(getSupportFragmentManager(), "用户名或密码");
    }
}