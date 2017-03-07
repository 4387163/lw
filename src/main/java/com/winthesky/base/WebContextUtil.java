package com.winthesky.base;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class WebContextUtil implements ApplicationContextAware, ServletContextAware {

	public static ServletContext servletContext = null;

	public static ApplicationContext applicationContext = null;

	@Override
	public void setServletContext(ServletContext sct) {
		servletContext = sct;
	}

	@Override
	public void setApplicationContext(ApplicationContext appCon) {
		applicationContext = appCon;
	}

	/**
	 * 根据指定的beanId获取bean
	 * 
	 * 获取spring配置文件中beanId对应的bean实例
	 * 
	 * @param <T>
	 * @param beanId
	 * @return 返回bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId) {
		return (T) WebContextUtil.applicationContext.getBean(beanId);
	}

	/**
	 * 按类型获取bean
	 * 
	 * @since: 2.3
	 * @param classType
	 *            bean类型
	 * @return bean实例
	 */
	public static <T> T getBeanByType(Class<T> classType) {
		return (T) WebContextUtil.applicationContext.getBean(classType);
	}

	/**
	 * 按类型获取所有Bean的名称
	 * 
	 * @author yujj
	 *
	 * @param classType
	 * @return
	 */
	public static String[] getBeanNamesByType(Class<?> classType) {
		return WebContextUtil.applicationContext.getBeanNamesForType(classType);
	}
}
