package com.zx.easyshop.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/2/13 0013.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void onClick() throws Exception {
        /**
         * 1、找到TextView
         * 2、找到button，设置点击时间
         * 3、检测TextView 显示的文本是不是我们期望的结果
         */
        //找到View:id、text

    }

}