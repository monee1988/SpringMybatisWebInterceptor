package com.monee1988.core.entity;


import com.monee1988.core.util.MD5Util;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;

/**
 * Created by wangchaodz on 15-12-26.
 * desc:
 */
public class Account extends BaseBean{

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
