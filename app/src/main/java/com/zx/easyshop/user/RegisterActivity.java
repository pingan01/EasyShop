package com.zx.easyshop.user;

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
import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;
import com.zx.easyshop.commons.LogUtils;
import com.zx.easyshop.commons.RegexUtils;
import com.zx.easyshop.model.UserResult;
import com.zx.easyshop.network.EasyShopApi;
import com.zx.easyshop.network.EasyShopClient;
import com.zx.easyshop.network.UICallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class RegisterActivity extends AppCompatActivity {

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
            activityUtils.showToast("账号为中文，字母或数字，长度为4~20，一个中文算2个长度");
            return;
        } else if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast("密码以数字或字母开头，长度在6~18之间，只能包含字符、数字和下划线");
            return;
        } else if (!TextUtils.equals(password, pwd_again)) {
            activityUtils.showToast("两次输入的密码不同！");
            return;
        }
        activityUtils.showToast("点击了注册按钮");

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
        Call call = EasyShopClient.getInstance().registerOrLogin(userName, password, EasyShopApi.BASE_URL + EasyShopApi.REGISTER);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureUI(Call call, IOException e) {

            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult result = new Gson().fromJson(body, UserResult.class);
                if (result.getCode() == 1) {
                    String hx_id = result.getData().getHx_ID();
                    LogUtils.e("环信ID：" + hx_id);
                    activityUtils.startActivity(LoginActivity.class);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}