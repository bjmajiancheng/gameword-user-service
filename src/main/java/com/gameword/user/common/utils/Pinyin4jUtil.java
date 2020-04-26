package com.gameword.user.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by majiancheng on 2020/4/26.
 */
public class Pinyin4jUtil {

    /**
     * 获取字符串首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

    /**
     * 获取首字符
     *
     * @param str
     * @return
     */
    public static String getPinYinFirstChar(String str) {
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                return String.valueOf(pinyinArray[0].charAt(0)).toUpperCase();
            } else {
                return "#";
            }
        }
        return "#";
    }
}
