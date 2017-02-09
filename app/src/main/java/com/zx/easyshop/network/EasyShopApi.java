package com.zx.easyshop.network;

/**
 * Created by Administrator on 2017/2/9 0009.
 * <p>
 * 易淘链接地址
 */

public class EasyShopApi {
    //服务器地址
    public static final String BASE_URL = "http://wx.feicuiedu.com:9094/yitao/";

    //图片的根路径
    public static final String IMAGE_URL = "http://wx.feicuiedu.com:9094/";

    //注册接口
    public static final String REGISTER = "UserWeb?method=register";

    //登录接口
    public static final String LOGIN = "UserWeb?method=login";

    //更新接口（更新昵称，更新头像）
    public static final String UPDATA = "UserWeb?method=update";

    //获取商品
    public static final String GETGOODS = "GoodsServlet?method=getAll";

    //获取商品详情
    public static final String DETAIL = "GoodsServlet?method=view";

    //删除商品
    public static final String DELETE = "GoodsServlet?method=delete";

    //上传商品
    public static final String UPLOADGOODS = "GoodsServlet?method=add";

    //获取好友列表
    public static final String GET_NAMES = "UserWeb?method=getNames";

    //查找好友
    public static final String GET_USER = "UserWeb?method=getUsers";

}
