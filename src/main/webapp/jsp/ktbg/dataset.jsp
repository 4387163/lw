<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Map<String, Object>> result = (List<Map<String, Object>>) request.getAttribute("result");
%>
<html>
<head>
<title>开题报告-数据集</title>
</head>
<body>
	<p>用户</p>
	<table border="1">
		<%
			if (!result.isEmpty()) {
		%>
		<tr>
			<%
				Map<String, Object> first = (Map<String, Object>) result.get(0);
					for (Map.Entry<String, Object> map : first.entrySet()) {
			%>
			<th><%=map.getKey()%></th>
			<%
				}
				}
			%>
		</tr>
		<%
			for (Map<String, Object> map : result) {
		%>
		<tr>
			<%
				for (Map.Entry<String, Object> entry : map.entrySet()) {
			%>
			<td><%=entry.getValue()%></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>