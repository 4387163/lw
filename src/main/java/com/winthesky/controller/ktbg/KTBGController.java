package com.winthesky.controller.ktbg;

import java.util.List;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winthesky.bo.BaseBusinessBO;

@Controller
@RequestMapping("/ktbg/")
public class KTBGController {
	
	@Autowired
	@Qualifier("baseBusinessBO")
	private BaseBusinessBO bo;
	
	@RequestMapping()
	public String index() {
		return "ktbg/index";
	}
	
	@RequestMapping("/view_ds")
	public String viewDataSet(HttpServletRequest request, @WebParam String ds) {
		List list = bo.getJdbcTemplate().queryForList("select * from " + ds);
		request.setAttribute("result", list);
		return "ktbg/dataset";
	}
}
