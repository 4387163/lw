<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Map<String, Double> map = (Map<String, Double>) request.getAttribute("result");
%>
<html>
<head>
<title>查询结果</title>
</head>
<body>
	<p>用户推荐结果：</p>
	<ol>
	<% for(Map.Entry<String, Double> entry : map.entrySet()) {%>
	<li><%=entry.getKey() + " : " + entry.getValue().toString() %></li>
	<% } %>
	</ol>
</body>
</html>