<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Map> result = (List<Map>) request.getAttribute("result");
%>
<html>
<head>
<title>新闻推荐结果</title>
</head>
<body>
	<p>新闻推荐结果：</p>
	<ol>
	<% for(Map map : result) {%>
		<li><%=map.get("news_id").toString() + " : " + map.get("score") %></li>
	<% } %>
	</ol>
</body>
</html>