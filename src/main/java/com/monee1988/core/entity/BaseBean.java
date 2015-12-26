package com.monee1988.core.entity;


import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by codePWX on 15-12-26.
 * desc:对象基本类
 */
public class BaseBean<T> implements Serializable{

    protected String id;//主键

    protected Date createDate;//创建时间

    protected Date updateDate;//修改时间

    protected Account createAccount;//创建帐号

    protected Account updateAccount;//创建帐号

    protected String rowState;//删除标志

    protected boolean isNewData;//是否是新数据

    protected Page<T> page;

    private static final String ROW_NORMAL = "0";//正常状态的数据状态
    private static final String ROW_DELETE = "1";//删除状态的数据状态

    /**
     * 新增对象前基本参数赋值
     * @param account
     */
    public void preInsert(Account account){
        this.createDate = new Date();
        this.updateDate = this.createDate;
        this.createAccount =account;
        this.updateAccount =account;
        this.rowState =ROW_NORMAL;
    }

    /**
     * 修改对象前基本参数赋值
     * @param account
     */
    public void preUpdate(Account account){
        this.updateDate = this.createDate;
        this.updateAccount =account;
    }

    /**
     * 删除对象前基本参数赋值
     * @param account
     */
    public void preDelete(Account account){
        this.updateDate = this.createDate;
        this.updateAccount =account;
        this.rowState = ROW_DELETE;
    }

    public boolean isNewData(){
       return StringUtils.isEmpty(this.id)||this.isNewData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Account getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(Account createAccount) {
        this.createAccount = createAccount;
    }

    public Account getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(Account updateAccount) {
        this.updateAccount = updateAccount;
    }

    public String getRowState() {
        return rowState;
    }

    public void setRowState(String rowState) {
        this.rowState = rowState;
    }

    public void setNewData(boolean newData) {
        isNewData = newData;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}

