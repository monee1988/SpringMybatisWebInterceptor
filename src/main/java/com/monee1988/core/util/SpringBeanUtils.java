package com.monee1988.core.util;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/** 
 * 获取Spring容器中Bean实例的工具类(Java泛型方法实现)。 
 */ 
public class SpringBeanUtils implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    /** 
     * 注入BeanFactory实例 
     */ 
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        setBeanFactoryValue(beanFactory);
	}

    private void setBeanFactoryValue(BeanFactory beanFactory) {
        SpringBeanUtils.beanFactory = beanFactory; 
	}

	/** 
     * 根据bean的名称获取相应类型的对象 
     * 
     * @param beanName 
     *            bean的名称 
     * @return Object类型的对象 
     */ 
    public static Object getBean (String beanName) { 
        return beanFactory.getBean(beanName); 
    }


    /** 
     * 根据bean的名称获取相应类型的对象，使用泛型，获得结果后，不需要强制转换为相应的类型 
     * 
     * @param clazz 
     *            bean的类型，使用泛型 
     * @return T类型的对象 
     */ 
    public static <T> T getBean (Class<T> clazz) { 

    	return beanFactory.getBean(clazz); 
    }
    
    public static <T> T getBean (Class<T> clazz,String beanName) { 
    	
        return beanFactory.getBean(beanName, clazz); 
    }

 }