package com.zx.easyshop.model;

/**
 * Created by Administrator on 2017/2/10 0010.
 */


public class UserResult {
    /**
     * "code"                  结果码
     * "msg"                   描述信息
     * "data": {               用户相关信息
     * "username": "xc62",
     * "name": "yt59856b15cf394e7b84a7d48447d16098",
     * "uuid": "0F8EC12223174657B2E842076D54C361",
     * "password": "123456"
     */
    private int code;
    private String msg;
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

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
}
