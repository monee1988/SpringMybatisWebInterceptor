/**
 * @(#){Account}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.entity;


import com.monee1988.core.util.MD5Util;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public class Account extends BaseBean<Account>{

    private String userName;

    private String passWord;

    public Account() {
    }


    @Override
    public void preInsert(Account account) {
        super.preInsert(account);
        if(!StringUtils.isEmpty(this.getUserName())&&StringUtils.isEmpty(this.getPassWord())){
            this.passWord = MD5Util.getMD5(this.userName+this.passWord);
        }
    }

    @Override
    public void preUpdate(Account account) {
        super.preUpdate(account);
        if(!StringUtils.isEmpty(this.getUserName())&&StringUtils.isEmpty(this.getPassWord())){
            this.passWord = MD5Util.getMD5(this.userName+this.passWord);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
