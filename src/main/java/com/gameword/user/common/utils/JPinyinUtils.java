package com.gameword.user.common.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang.StringUtils;

/**
 * @author: majiancheng
 * @create: 2020-06-22 17:32
 **/
public class JPinyinUtils {
    public static void main(String[] args) {
        String str = covertToPinyinString("国乾信息咨询有限公司");
        System.out.println(str);
    }

    /**
     * 将中文转换为拼音
     *
     * @param str
     * @return
     */
    public static String covertToPinyinString(String str) {
        return covertToPinyinString(str, StringUtils.EMPTY);
    }

    /**
     * 将中文转换为拼音
     *
     * @param str
     * @return
     */
    public static String covertToPinyinString(String str, String split) {
        if(StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }

        try {
            str = PinyinHelper.convertToPinyinString(str, split, PinyinFormat.WITHOUT_TONE);
            str = StringUtils.isEmpty(str) ? str : str.toLowerCase();
            return str;
        } catch(PinyinException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * 获取首字母
     *
     * @param str
     * @return
     */
    public static String covertToFirstPinyinString(String str) {
        if(StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }

        try {
            str = PinyinHelper.getShortPinyin(str);
            str = StringUtils.isEmpty(str) ? str : str.toLowerCase();
            return str;
        } catch(PinyinException e) {
            e.printStackTrace();

            return "";
        }
    }
}
