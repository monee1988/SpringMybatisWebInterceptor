package com.monee1988.core.service;

import com.monee1988.core.entity.Account;
import com.monee1988.core.entity.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public interface BaseService<T,PK extends Serializable> {

    /**
     * 查询分页
     * @param page 分页信息
     * @param <> 参数对象
     * @return 分页对象
     */
    public Page<T> findPage(Page<T> page, T t);


    /**
     * 根据参数查询集合
     * @param t 参数对象
     * @return
     */
    public List<T> findList(T t);

    /**
     * 查询
     * @param id 主键id
     * @return
     */
    public T findEntity(PK id);

    /**
     * 删除对象
     * @param id 主键Id
     * @return
     */
    public int deleteEntityById(PK id);

    /**
     * 删除对象
     * @param ids 主键数组
     * @return
     */
    public int deleteEntityByIds(List<PK> ids);

    /**
     * 删除对象
     * @param pid 外键
     * @return
     */
    public int deleteEntityByFid(PK pid);

    /**
     * 新增对象
     * @param t
     * @return
     */
    public int insertEntity(T t,Account account);

    /**
     * 批量新增数据
     * @param ts 对象集合
     * @return
     */
    public int insertBachEntity(List<T> ts,Account account);

    /**
     * 修改对象
     * @param t 参数对象
     * @return
     */
    public int updateEntity(T t,Account account);

}
