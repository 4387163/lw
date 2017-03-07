package com.winthesky.bo;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.winthesky.base.WebContextUtil;

@Component
public class BaseBusinessBO {

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate(getDataSource());
		}
		return jdbcTemplate;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = WebContextUtil.getBean("dataSource");
		}
		return dataSource;
	}

}
