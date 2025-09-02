package com.wen.util.general;

/**
 * 
 * @email wendb.top@aliyun.com
 * @date 2020/10/29 20:02
 * @description 自定义工具类
 */

public class MyUtils {
    public static String getCode(int length) {
        String str ="123456";
        return str.substring(str.length() - length);
    }

    private MyUtils() {
    }
}
