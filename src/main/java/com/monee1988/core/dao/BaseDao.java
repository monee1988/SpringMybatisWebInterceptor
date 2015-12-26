package com.monee1988.core.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangchaodz on 15-12-26.
 * desc:
 */
public interface BaseDao<T,PK extends Serializable> {

    /**
     * 新增数据
     * @param t
     * @return
     */
    public int saveEntity(T t);

    /**
     * 批量新增数据
     * @param t
     * @return
     */
    public int saveBatch(List<T> t);

    /**
     * 更新数据
     * @param t
     * @return
     */
    public int updateEntity(T t);

    /**
     * 按主键ID查询对象
     * @param id
     * @return
     */
    public T findEntityById(PK id);

    /**
     * 根据条件查询集合
     * @param t
     * @return
     */
    public List<T> findList(T t);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public int deleteById(PK id);

    /**
     * 根据外键删除
     * @param pid
     * @return
     */
    public int deleteByPId(PK pid);
    /**
     * 根据主键集合删除数据
     * @param ids
     * @return
     */
    public int deleteByIds(List<PK> ids);




}
