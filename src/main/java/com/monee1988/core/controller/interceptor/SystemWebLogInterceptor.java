/**
 * @(#){SystemWebLogInterceptor}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.controller.interceptor;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.monee1988.core.util.AjaxUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public class SystemWebLogInterceptor extends HandlerInterceptorAdapter{

    private static final Logger logger = LoggerFactory.getLogger(SystemWebLogInterceptor.class);

    /**
     * URL请求前的拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(logger.isDebugEnabled()){

            if(AjaxUtils.isAjaxRequest(request)){
				Map<String, Object> map = request.getParameterMap();
            	for (Entry<String, Object> entry : map.entrySet()) {
					System.out.println(entry.getKey()+":"+entry.getValue().toString());
				}
            	logger.debug("AJAX Request URL is [{}]",request.getRequestURI());
            }else{
                logger.debug("VIEW Request URL is [{}]",request.getRequestURI());
            }
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * URL请求中的拦截
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(logger.isDebugEnabled()&&!StringUtils.isEmpty(modelAndView)){
            logger.debug("Request Return VIEW is [{}]",modelAndView.getViewName());
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * URL请求后的拦截
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
