package com.zx.easyshop.model;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class LoginResult {

    private int code;
    private String msg;
    private User data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
