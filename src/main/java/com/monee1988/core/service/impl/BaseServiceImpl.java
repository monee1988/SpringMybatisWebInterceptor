/**
 * @(#){BaseServiceImpl}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.service.impl;

import com.monee1988.core.dao.BaseDao;
import com.monee1988.core.entity.Account;
import com.monee1988.core.entity.BaseBean;
import com.monee1988.core.entity.Page;
import com.monee1988.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public class BaseServiceImpl<T extends BaseBean<T>,PK extends Serializable,Dao extends BaseDao<T, PK>> implements BaseService<T,PK>{


    @Autowired
    protected Dao dao;


    /**
     * 分页查询实现
     */
    public Page<T> findPage(Page<T> page, T t) {

        t.setPage(page);
        page.setResult(findList(t));

        return page;
    }

    /**
     * 集合查询实现
     */
    public List<T> findList(T t) {

        return dao.findList(t);
    }

    /**
     * 单实体查询实现
     */
    public T findEntity(PK id) {

        return dao.findEntityById(id);
    }

    /**
     * 根据主键删除数据
     */
    public int deleteEntityById(PK id) {

        return dao.deleteById(id);
    }

    /**
     * 根据主键集合删除数据
     */
    public int deleteEntityByIds(List<PK> ids) {

        return dao.deleteByIds(ids);
    }

    /**
     * 根据外键删除数据
     */
    public int deleteEntityByFid(PK pid) {

        return dao.deleteByPId(pid);
    }

    /**
     * 新增数据
     */
    public int insertEntity(T t, Account account) {

        t.preInsert(account);

        return dao.saveEntity(t);
    }

    /**
     * 批量新增数据
     */
    public int insertBachEntity(List<T> ts, Account account) {

        List<T> datas = new LinkedList<T>();

        for (T t : ts) {
            t.preInsert(account);
            datas.add(t);
        }

        return dao.saveBatch(datas);
    }

    /**
     * 更新数据
     */
    public int updateEntity(T t, Account account) {

        t.preUpdate(account);

        return dao.updateEntity(t);
    }
}
