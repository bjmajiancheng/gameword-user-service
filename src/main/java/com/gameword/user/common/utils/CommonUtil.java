package com.gameword.user.common.utils;

import com.gameword.user.common.constant.CommonConstant;

/**
 * Created by majiancheng on 2019/10/23.
 */
public class CommonUtil {

    public static String defaultString(Object obj, String defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        return String.valueOf(obj);
    }

    public static Integer defaultInteger(Object obj, Integer defaultVal) {
        if(obj == null) {
            return defaultVal;
        }

        return Integer.parseInt(String.valueOf(obj));
    }

    /**
     * 用户默认昵称
     *
     * @param nickName
     * @return
     */
    public static String defaultNickName(String nickName) {
        return defaultString(nickName, CommonConstant.DEFAULT_NICK_NAME);
    }

    public static String defaultHeadImage(String headImage) {
        return defaultString(headImage, CommonConstant.DEFAULT_HEAD_IMAGE);
    }
}
