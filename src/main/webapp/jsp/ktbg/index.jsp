<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<title>开题报告</title>
</head>
<body>
	<p>查看实验数据集</p>
	<ul>
		<li><a href="/ktbg/view_ds?ds=user">用户数据集</a></li>
		<li><a href="/ktbg/view_ds?ds=news">新闻数据集</a></li>
		<li><a href="/ktbg/view_ds?ds=news_rating">新闻评价数据集</a></li>
	</ul>
	<p>推荐测试</p>
	<ul>
		<li>
			<form action="/ktbg/recommend/user">
				输入用户ID：<input type="text" name="userID"> <input
					type="submit" value="查询推荐用户">
			</form>
		</li>
		<li>
			<form action="/ktbg/recommend/news">
				输入用户ID：<input type="text" name="userID"> <input
					type="submit" value="查询推荐新闻">
			</form>
		</li>
	</ul>
</body>
</html>