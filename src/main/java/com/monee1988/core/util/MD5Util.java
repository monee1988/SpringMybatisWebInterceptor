/**
 * @(#){MD5Util}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public class MD5Util {

    public static String getMD5(String originString){
        String result="";
        try {
            // 指定加密的方式为MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 进行加密运算
            byte bytes[] = md.digest(originString.getBytes());
            for (int i = 0; i < bytes.length; i++) {
                // 将整数转换成十六进制形式的字符串 这里与0xff进行与运算的原因是保证转换结果为32位
                String str = Integer.toHexString(bytes[i] & 0xFF);
                if (str.length() == 1) {
                    str += "F";
                }
                result += str;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static String createId(){
    	return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    } 
    
    public static void main(String[] args){

        System.out.print(getMD5("adminpassword"));
    }
}
