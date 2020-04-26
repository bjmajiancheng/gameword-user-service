package com.gameword.user.common.utils;

import io.rong.RongCloud;
import io.rong.methods.user.User;
import io.rong.models.*;
import io.rong.models.response.TokenResult;
import io.rong.models.response.UserResult;
import io.rong.models.user.UserModel;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * Created by majiancheng on 2020/4/26.
 */
public class RongyunUtil implements Serializable{

    private static final long serialVersionUID = 1440763827618391064L;

    @Value("${rongyun.appKey}")
    private String appKey;

    @Value("${rongyun.appSecret}")
    private String appSecret;

    @Value("${rongyun.api}")
    private String api;

    private RongCloud rongCloud;

    private RongCloud getRongCloud() {
        if(this.rongCloud == null) {
            this.rongCloud = RongCloud.getInstance(this.appKey, this.appSecret);
        }

        return this.rongCloud;
    }

    /**
     * 注册用户信息
     *
     * @param userId
     * @param nickName
     * @param headImage
     * @return
     */
    public TokenResult register(Integer userId, String nickName, String headImage) {
        try {
            UserModel user = generUserModel(userId, nickName, headImage);

            return getRongCloud().user.register(user);

        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param nickName
     * @param headImage
     * @return
     */
    public io.rong.models.Result update(Integer userId, String nickName, String headImage) {
        try {
            UserModel user = generUserModel(userId, nickName, headImage);

            return getRongCloud().user.update(user);

        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public UserResult get(Integer userId, String nickName, String headImage) {
        try {
            UserModel user = generUserModel(userId, nickName, headImage);

            return getRongCloud().user.get(user);

        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 生成融云用户信息
     *
     * @param userId
     * @param nickName
     * @param headImage
     * @return
     */
    public UserModel generUserModel(Integer userId, String nickName, String headImage) {
        return new UserModel()
                .setId(String.valueOf(userId))
                .setName(nickName)
                .setPortrait(headImage);
    }

    public static void main(String[] args) throws Exception {

        RongCloud rongCloud = RongCloud.getInstance("p5tvi9dspqek4", "YQ0XwWL6t6Mjcv");
        // 自定义 api 地址方式
        // RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret,api);
        // 使用 百度 HTTPDNS 获取最快的 IP 地址进行连接
        // BaiduHttpDNSUtil.setHostTypeIp("account_id", "secret", rongCloud.getApiHostType());

        // 设置连接超时时间
        // rongCloud.getApiHostType().setConnectTimeout(10000);
        // 设置读取超时时间
        // rongCloud.getApiHostType().setReadTimeout(10000);
        // 获取备用域名List
        // List<HostType> hosttypes = rongCloud.getApiHostListBackUp();
        // 设置连接、读取超时时间
        // for (HostType hosttype : hosttypes) {
        //     hosttype.setConnectTimeout(10000);
        //     hosttype.setReadTimeout(10000);
        // }

        User User = rongCloud.user;

        /**
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#register
         *
         * 注册用户，生成用户在融云的唯一身份标识 Token
         */
        UserModel user = new UserModel()
                .setId("123123")
                .setName("肖恩")
                .setPortrait("http://www.rongcloud.cn/images/logo123.png");
        TokenResult result = User.register(user);
        System.out.println("getToken:  " + result.toString());

        /**
         *
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#refresh
         *
         * 刷新用户信息方法
         */
        io.rong.models.Result refreshResult = User.update(user);
        System.out.println("refresh:  " + refreshResult.toString());

        /**
         *
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#get
         *
         * 查询用户信息方法
         */
        UserResult userResult = User.get(user);
        System.out.println("getUserInfo:  " + userResult.toString());
    }
}
