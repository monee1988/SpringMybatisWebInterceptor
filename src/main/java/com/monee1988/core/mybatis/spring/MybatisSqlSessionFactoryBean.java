/**
 * @(#){MybatisSqlSessionFactoryBean}.java 1.0 {15/12/26}
 *
 * Copyright 2015 greatpwx@126.com, All rights reserved.
 * Use is subject to license terms.
 * https://github.com/monee1988/SpringMybatisWebInterceptor
 */
package com.monee1988.core.mybatis.spring;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codePWX on 15-12-26.
 * desc:
 */
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**

     * 按通配符获取对应的文件包

     */
    @Override
    public void setTypeAliasesPackage(String typeAliasesPackages) {

        List<String> typeAliasesPackageList = Arrays.asList(typeAliasesPackages.split(","));
        List<String> list  = new ArrayList<String>();
        for (String typeAliasesPackage : typeAliasesPackageList) {
            list.addAll(getPackages(typeAliasesPackage));
            logger.debug("typeAliasesPackage: {}", typeAliasesPackage);
        }

        if(list!=null&&list.size()>0){
            super.setTypeAliasesPackage(listToString(list));
        }else{
            logger.warn("typeAliasesPackage: {}，未找到任何包", typeAliasesPackages);
        }
    }

    /**

     * @param typeAliasesPackage

     * @return

     */
    private List<String> getPackages(String typeAliasesPackage) {

        String baseUrl = typeAliasesPackage.replaceAll("\\.", "/");
        String rootUrl ;
        if(baseUrl.contains("*")){
            rootUrl = baseUrl.substring(0, baseUrl.indexOf("*"));
        }else{
            rootUrl = baseUrl;
        }
        List<String> packagePathList = new LinkedList<String>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources =resourcePatternResolver.getResources(baseUrl);
            for (Resource resource : resources) {
                String packageName = resource.getURL().toString();
                packageName =	packageName.substring(packageName.indexOf(rootUrl),packageName.length()-1).replaceAll("/", ".");
                logger.debug("packageName: {}", packageName);
                packagePathList.add(packageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packagePathList;
    }


    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

}
