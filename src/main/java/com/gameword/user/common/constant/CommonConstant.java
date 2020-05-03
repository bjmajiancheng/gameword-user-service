package com.gameword.user.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * Created by majiancheng on 2019/10/23.
 */
public class CommonConstant {

    /** 默认昵称 */
    public static final String DEFAULT_NICK_NAME = "未知";

    /** 默认头图 */
    public static final String DEFAULT_HEAD_IMAGE = "/gameword-user-service/upload/system/image/20200426/20200426102736_331_20200426102735.jpg";


    public static List<Integer> ALLOW_THIRD_TYPE = Arrays.asList(1, 2);

    /**
     * 标签分类
     */
    /** 系统默认分类 */
    public final static int SYSTEM_LABEL_CATEGORY = 1;

    /** 用户标签分类 */
    public final static int USER_LABEL_CATEGORY = 2;

    /** 圈子标签分类 */
    public final static int CIRCLE_LABEL_CATEGORY = 3;

    /** 俱乐部标签分类 */
    public final static int COMPANY_LABEL_CATEGORY = 4;

    /** 帖子标签分类 */
    public final static int POST_LABEL_CATEGORY = 5;

}
