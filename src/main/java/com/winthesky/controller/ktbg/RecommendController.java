package com.winthesky.controller.ktbg;

import java.util.List;
import java.util.Map;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winthesky.bo.ktbg.RecommendBO;

@Controller
@RequestMapping("/ktbg/recommend")
public class RecommendController {
	
	@Autowired
	private RecommendBO bo;
	
	@RequestMapping()
	public String index(HttpServletRequest request) {
		return "ktbg/recommend";
	}

	@RequestMapping("user")
	public String recommendUsers(HttpServletRequest request, @WebParam String userID) {
		Map<String, Double> map = bo.recommendUsers(userID);
		request.setAttribute("result", map);
		return "ktbg/recmmd_user_result";
	}
	
	@RequestMapping("news")
	public String recommendNews(HttpServletRequest request, @WebParam String userID) {
		List list = bo.recommendNews(userID);
		request.setAttribute("result", list);
		return "ktbg/recmmd_news_result";
	}

}
