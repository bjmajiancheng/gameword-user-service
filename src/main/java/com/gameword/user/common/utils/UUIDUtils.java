package com.gameword.user.common.utils;

import java.util.UUID;

/**
 * Created by majiancheng on 2019/10/22.
 */
public class UUIDUtils {

    public static String generUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(UUIDUtils.generUUID().length());
        System.out.println("13717689765".substring(3, 7));
    }
}
