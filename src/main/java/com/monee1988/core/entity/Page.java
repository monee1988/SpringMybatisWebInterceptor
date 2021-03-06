/**
 * @(#){Page}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/**
 * 分页对象
 * Created by codePWX on 15-12-26.
 * @param <T> Page中记录的类型.
 */
public class Page<T> implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*公共变量 默认正序**/
    public static final String ASC = "asc";

    /*公共变量 默认倒序*/
    public static final String DESC = "desc";

    /*默认的分页页码容量*/
    public static final int DEFAULT_PAGESIZE =10;

    /*默认页码*/
    public static final int DEFAULT_PAGENO= 1;

    /*页码*/
    protected int pageNo = DEFAULT_PAGENO;

    /*每一页数据容量*/
    protected int pageSize = DEFAULT_PAGESIZE;

    /*排序*/
    protected String orderBy = null;

    /*排序方式*/
    protected String order = null;
    protected boolean autoCount = true;

    /*显示的页码列表的开始索引*/
    private int startPageIndex;

    /*显示的页码列表的结束索引*/
    private int endPageIndex;

    /*总页数*/
    private int pageCount;

    private Map<?,?> extend; // page扩展信息

    /*返回结果*/
    private List<T> resultList = new ArrayList<T>();

    /*总记录数*/
    private long totalCount = -1;

    /*当前查询的SQL*/
    private String sql;

    public Page() {

    }

    public Page<T> end() {
        // 1, 总页�?
        pageCount = ((int) this.totalCount + pageSize - 1) / pageSize;
        // 2, startPageIndex（显示的页码列表的开始索引）与endPageIndex（显示的页码列表的结束索引）
        // a, 总页码不大于10�?
        if (pageCount <= 10) {
            startPageIndex = 1;
            endPageIndex = pageCount;
        }
        // b, 总码大于10�?
        else {
            // 在中间，显示前面4个，后面5�?
            startPageIndex = pageNo - 4;
            endPageIndex = pageNo + 5;

            // 前面不足4个时，显示前10个页�?
            if (startPageIndex < 1) {
                startPageIndex = 1;
                endPageIndex = 10;
            }
            // 后面不足5个时，显示后10个页�?
            else if (endPageIndex > pageCount) {
                endPageIndex = pageCount;
                startPageIndex = pageCount - 10 + 1;
            }
        }
        return this;
    }

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize, int totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    //-- 分页参数访问函数 --//
    public Page(HttpServletRequest request, HttpServletResponse response) {
    	request.getParameterMap();
    	String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		if (!StringUtils.isEmpty(pageNoStr)&&!"null".equals(pageNoStr)) {
			this.pageNo = Integer.valueOf(pageNoStr);
		}else{
			this.pageNo = DEFAULT_PAGENO;
		}
		if (!StringUtils.isEmpty(pageSizeStr)&&!"null".equals(pageSizeStr)) {
			this.pageSize = Integer.valueOf(pageSizeStr);
		}else{
			this.pageSize = DEFAULT_PAGESIZE;
		}
	}

	/**
     * 获得当前页的页号
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页的页号,低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     * 返回Page对象自身的setPageNo函数,可用于连续设置�?
     */
    public Page<T> pageNo(final int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     * 获得每页的记录数默认1.
     */
    public int getPageSize() {

        return pageSize;
    }

    /**
     * 设置每页的记录数
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 返回Page对象自身的setPageSize函数,可用于连续设置�?
     */
    public Page<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 根据pageNo和pageSize计算当前页第�?��记录在�?结果集中的位�?序号�?�?��.
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    /**
     * 获得排序字段,无默认�?. 多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 返回Page对象自身的setOrderBy函数,可用于连续设置�?
     */
    public Page<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     * 获得排序方向, 无默认�?.
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式�?
     *
     * @param order 可�?值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        String lowcaseOrder = String.valueOf(order).toLowerCase();

        //�?��order字符串的合法�?
//        String[] orders = lowcaseOrder.split(lowcaseOrder, ',');
//        for (String orderStr : orders) {
//            if (!DESC.equals(orderStr) && !ASC.equals( orderStr)) {
//                throw new IllegalArgumentException("  " + orderStr + " ");
//            }
//        }
        this.order = lowcaseOrder;
    }

    /**
     * 返回Page对象自身的setOrder函数,可用于连续设置�?
     */
    public Page<T> order(final String theOrder){
    	setOrder(theOrder);
        return this;
    }

    /**
     * 是否已设置排序字�?无默认�?.
     */
    public boolean isOrderBySetted() {
        return (!StringUtils.isEmpty(orderBy) && !StringUtils.isEmpty(order));
    }

    /**
     * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为false.
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     * 设置查询对象时是否自动先执行count查询获取总记录数.
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    /**
     * 返回Page对象自身的setAutoCount函数,可用于连续设置�?
     */
    public Page<T> autoCount(final boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页�? 默认值为-1.
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }

        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页码
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页码
     * 当前页为首页时返回首页序�?
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }


    /**
     * 根据pageNo和pageSize计算当前页第�?��记录在�?结果集中的位�?序号�?�?��.
     * 用于Mysql,Hibernate.
     */
    public int getOffset() {
        return ((pageNo - 1) * pageSize);
    }

    /**
     * 根据pageNo和pageSize计算当前页第�?��记录在�?结果集中的位�?序号�?�?��.
     * 用于Oracle.
     */
    public int getStartRow() {
        return getOffset() + 1;
    }

    /**
     * 根据pageNo和pageSize计算当前页最后一条记录在总结果集中的位置, 序号�?�?��.
     * 用于Oracle.
     */
    public int getEndRow() {
        return pageSize * pageNo;
    }

    public int getStartPageIndex() {
        return startPageIndex;
    }

    public void setStartPageIndex(int startPageIndex) {
        this.startPageIndex = startPageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Map<?,?> getExtend() {
        return extend;
    }

    public void setExtend(Map<?,?> extend) {
        this.extend = extend;
    }

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}


}