package com.zx.easyshop.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class User {
    /**
     * "username": "xc62",           用户名
     * "name": "yt59856b15cf394e7b84a7d48447d16098", 环信ID
     * "uuid": "0F8EC12223174657B2E842076D54C361",  用户表主键
     * "password": "123456"  用户密码
     */
    @SerializedName("username")
    private String name;
    @SerializedName("name")
    private String hx_ID;
    @SerializedName("uuid")
    private String table_ID;
    private String password;
    @SerializedName("other")
    private String image;
    private String nickname;//登录时的昵称

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHx_ID() {
        return hx_ID;
    }

    public void setHx_ID(String hx_ID) {
        this.hx_ID = hx_ID;
    }

    public String getTable_ID() {
        return table_ID;
    }

    public void setTable_ID(String table_ID) {
        this.table_ID = table_ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
