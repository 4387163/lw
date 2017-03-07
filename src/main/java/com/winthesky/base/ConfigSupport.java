package com.winthesky.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class ConfigSupport extends PropertyPlaceholderConfigurer {

	public static final boolean GLOABLE_CFG_BEAN_LAZY_INIT = false;

	private Properties properties = new Properties();
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private static ConfigSupport config = new ConfigSupport();
	private String fileEncoding = "utf-8";

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * 隐藏构造函数
	 */
	private ConfigSupport() {

	}

	/**
	 * 获取单例
	 * 
	 * @param
	 * @return
	 */
	public static ConfigSupport getInstance() {

		return config;
	}

	@Override
	public void setLocation(Resource resource) {
		if (resource == null) {
			return;
		}

		super.setLocation(resource);

		loadPropertiesFromResource(new EncodedResource(resource, this.fileEncoding));
	}

	@Override
	public void setLocations(Resource[] resources) {
		if (resources == null || resources.length == 0) {
			return;
		}
		super.setLocations(resources);
		for (int i = 0; i < resources.length; i++) {

			// this.loadPropertiesFromResource(resources[i]);
			this.loadPropertiesFromResource(new EncodedResource(resources[i], this.fileEncoding));
		}
	}

	private void loadPropertiesFromResource(EncodedResource resource) {
		InputStream inputStream = null;
		Reader reader = null;
		try {
			/*
			 * inputStream = resource.getInputStream();
			 * properties.load(inputStream);
			 */
			reader = resource.getReader();
			propertiesPersister.load(properties, reader);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static boolean isUaisEnable() {
		return Boolean.valueOf(System.getProperty("UAIS_ENABLE"));
	}
}
