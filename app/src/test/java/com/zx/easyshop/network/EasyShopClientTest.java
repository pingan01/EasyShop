package com.zx.easyshop.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/2/13 0013.
 * 创建的EasyShopClient的测试类
 */
public class EasyShopClientTest {

    /**
     * @throws Exception
     * @Before：测试方法执行之前会执行；作用：初始化工作
     * @After：测试方法之后会执行；作用：释放资源--对象置空
     * @Test：要测试的方法；测试用例
     * @BeforeClass：只会被执行一次的静态方法;数据库的连接等
     * @AfterClass：只会被执行一次的静态方法；数据库断开连接等
     *
     * 执行顺序为：@BeforeClass --> @Before --> @Test --> @After --> @AfterClass
     */
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void registerOrLogin() throws Exception {
      Call call=EasyShopClient.getInstance().registerOrLogin("pingan03","888888",EasyShopApi.BASE_URL + EasyShopApi.REGISTER);
        Response response=call.execute();
        assertNotNull(response);
    }

    @After
    public void tearDown() throws Exception {

    }


}