package com.winthesky.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class RequestFilter extends OncePerRequestFilter {
	
	private List<String> excludedUrls = new ArrayList<String>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		String str = ConfigSupport.getInstance().getProperty("UNNEED_SESSION_URIS");
		String[] uris = str.split(",");
		if ("/".equals(requestUri)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (request.getSession().getAttribute("user") == null) {
			boolean needLogin = true;
			for (String uri : uris) {
				if (requestUri.indexOf(uri) != -1) {
					needLogin = false;
					break;
				}
			}

			if (needLogin) {
				response.sendRedirect("/user/login");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}
