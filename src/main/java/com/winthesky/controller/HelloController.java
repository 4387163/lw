package com.winthesky.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class HelloController {

	@RequestMapping("hello")
	public String hello(HttpServletRequest request) {
		System.out.println("hello...");
		request.setAttribute("name", "winthesky");
		return "hello";
	}

}
