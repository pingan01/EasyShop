package com.zx.easyshop.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对用户信息本地保存
 */
public class CachePreferences {

    private static final String NAME = CachePreferences.class.getSimpleName();
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PWD = "userPwd";
    private static final String KEY_USER_HX_ID = "userHxID";
    private static final String KEY_USER_TABLE_ID = "userUuid";
    private static final String KEY_USER_HEAD_IMAGE = "userHeadImage";
    private static final String KEY_USER_NICKNAME = "userNickName";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private CachePreferences() {
    }

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void clearAllData() {
        editor.clear();
        editor.apply();
    }

    public static void setUser(User user) {
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_PWD, user.getPassword());
        editor.putString(KEY_USER_HX_ID, user.getHx_ID());
        editor.putString(KEY_USER_TABLE_ID, user.getTable_ID());
        editor.putString(KEY_USER_HEAD_IMAGE, user.getImage());
        editor.putString(KEY_USER_NICKNAME, user.getNickname());

        editor.apply();
    }

    public static User getUser() {
        User user = new User();
        user.setName(preferences.getString(KEY_USER_NAME, null));
        user.setPassword(preferences.getString(KEY_USER_PWD, null));
        user.setHx_ID(preferences.getString(KEY_USER_HX_ID, null));
        user.setTable_ID(preferences.getString(KEY_USER_TABLE_ID, null));
        user.setImage(preferences.getString(KEY_USER_HEAD_IMAGE, null));
        user.setNickname(preferences.getString(KEY_USER_NICKNAME, null));
        return user;
    }

}
