/**
 * @(#){PageInterceptor}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.mybatis.pageinterceptor;

import cn.org.rapid_framework.ibatis3.plugin.OffsetLimitInterceptor;
import cn.org.rapid_framework.jdbc.dialect.Dialect;
import com.monee1988.core.entity.Page;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 	扩展修改OffsetLimitInterceptor分页拦截器
 * 
 *  <pre>
 * 		&lt;property name="plugins"> 
 * 			&lt;array> 
 * 				&lt;!-- 分页拦截器 -->
 * 				&lt;bean class="com.platform.mybatis.pageinterceptor.PageInterceptor">
 *					&lt;property name="dialectClass" value="cn.org.rapid_framework.jdbc.dialect.MySQLDialect"/>
 * 				&lt;/bean>
 *  		&lt;/array>
 * 		&lt;/property>
 * 	</pre>
 * 
 * @author codePwx
 * @time 2015-12-21
 *
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})})
public class PageInterceptor extends OffsetLimitInterceptor implements Interceptor {

	private Logger logger =LoggerFactory.getLogger(getClass());

	protected static final String PAGE = "page";

	protected static int MAPPED_STATEMENT_INDEX = 0;

	protected static int PARAMETER_INDEX = 1;
	
	protected static int ROWBOUNDS_INDEX = 2;
	
	protected static int RESULT_HANDLER_INDEX = 3;

	protected Dialect dialect;

	public Object intercept(Invocation invocation) throws Throwable {

		processMybatisIntercept(invocation.getArgs(),invocation);

		return invocation.proceed();
	}

	void processMybatisIntercept(final Object[] queryArgs,Invocation invocation) {
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		
		Page<?> page  = null;
		
		if(parameter !=null){
			page = convertParameter(page,parameter);
		}

		if (dialect.supportsLimit() && page !=null) {
			
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
				
			final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
			int offset = rowBounds.getOffset();
			int limit = rowBounds.getLimit();
			offset = page.getOffset();
			limit = page.getPageSize();
			
			CachingExecutor executor = (CachingExecutor) invocation.getTarget();
			
			Transaction transaction = executor.getTransaction();
			try {
				Connection connection = transaction.getConnection();
				/**
				 * 查询总记录数
				 */
				this.setTotalRecord(page, ms, connection,parameter);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (dialect.supportsLimitOffset()) {
				
				sql = dialect.getLimitString(sql, offset, limit);
				offset = RowBounds.NO_ROW_OFFSET;
				
			} else {
				
				sql = dialect.getLimitString(sql, 0, limit);
				
			}
			limit = RowBounds.NO_ROW_LIMIT;
			
			queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
			
			BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
			
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
			
		}
	}

	/**
	 *
	 * @param page
	 * @param mappedStatement
	 * @param connection
	 * @param parameterObject
     */
	private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, Connection connection, Object parameterObject) {
		
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,	parameterMappings, parameterObject);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		logger.debug("Total count SQL [{}] ", countSql.toString());
		logger.debug("Total count Parameters: {} ", parameterObject);

		try {
			pstmt = connection.prepareStatement(countSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				logger.debug("Total count: {}", totalRecord);
				page.setTotalCount(totalRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		
		return "select count(1) from (" + sql + ") AS total";
	}

	/**
	 * @param page
	 * @param parameter
	 * @return
	 */
	private Page<?> convertParameter(Page<?> page, Object parameter) {
		
		if (parameter instanceof Page<?>) {
			return (Page<?>) parameter;	
		}
		if(parameter instanceof Map<?, ?>){
			return (Page<?>) ((Map<?,?>) parameter).get(PAGE);
		}
		
		return currentGetFiled(parameter, PAGE);
	}

	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
				boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	// see: MapperBuilderAssistant
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());

		builder.timeout(ms.getTimeout());

		builder.parameterMap(ms.getParameterMap());

		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());

		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}

	/**
	 * @param dialectClass
	 *    the dialectClass to set
	 */
	public void setDialectClass(String dialectClass) {
		try {
			dialect = (Dialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
		}
		logger.debug(this.getClass().getSimpleName()+ ".dialect=[{}]", dialect.getClass().getSimpleName());
	}

	

	/**
	 * @return 
	 * 
	 */
	private static Page<?> currentGetFiled(Object object,String param) {
		
		Field pageField= ReflectionUtils.findField(object.getClass(), param);

		try {
			boolean accessible = pageField.isAccessible();
			pageField.setAccessible(Boolean.TRUE);
			Page<?> page = (Page<?>) pageField.get(object);
			pageField.setAccessible(accessible);
			if(page!=null){
				return page;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
		
	}
}