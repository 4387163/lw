<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<title>导航</title>
</head>
<body>
	<form action="/ktbg/recommend/user">
		输入用户ID：<input type="text" name="userID">
		<input type="submit" value="查询推荐用户">
	</form>
	
	<form action="/ktbg/recommend/news">
		输入用户ID：<input type="text" name="userID">
		<input type="submit" value="查询推荐新闻">
	</form>
</body>
</html>