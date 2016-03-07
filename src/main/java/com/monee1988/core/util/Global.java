package com.monee1988.core.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.alibaba.fastjson.JSON;

/**
 * 全局属性配置读取
 * @author root
 *
 */
public class Global{


	private static Logger logger = LoggerFactory.getLogger(Global.class);

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	private static Map<String, String> map = new LinkedHashMap<String, String>();
	
	public static Global getInstance(){
		return global;
	}
	
	/**
	 * 获取系统管理根路径
	 * @return
	 */
	public static String getAdminPath(){
		return getConfig("adminPath");
	}

	/**
	 * 获取配置值
	 * @param key 
 	 * @return value
 	 * ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		
		String value = map.get(key);
		if (value == null){
			value = ConfigUtil.getStringValue(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		
		return value;
	}
	
	/**
	 * 获取配置值
	 * @param key  参数Key
	 * @param defaultValue 默认值 
 	 * @return value
	 */
	public static String getConfig(String key, String defaultValue) {
		String value = getConfig(key);
		if(StringUtils.isEmpty(value)) return defaultValue;
		return value;
	}
	
	/**
	 * 将Object转换为JSONString
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		
		try {
			return JSON.toJSONString(object);
		} catch (Exception e) {
			logger.warn("write to json string error:" + object, e);
		}
		return null;
	}

	public static String getProjectPath() {
    	// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
	
	public static int getRetryTime() {
		String times = getConfig("retrytime", "5");
		try {
			Integer systemRetryTime = Integer.valueOf(times);
			return systemRetryTime.intValue();
		} catch (Exception ex) {
			ex.fillInStackTrace();
		}
		return 5;
	}

	public static String getCompanyName() {
		return getConfig("projectName", "Monee");
	}

	public static void main(String[] args) {
		Global global = Global.getInstance();
		System.out.println(global.getAdminPath());
	}
}
