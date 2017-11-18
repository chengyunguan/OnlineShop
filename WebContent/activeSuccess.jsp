<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>激活成功</title>
<script type="text/javascript">
    var intervalid = setInterval("showTime()", 1000);
    var s = 10
    function showTime() {
		if (s == 0) {
		    window.location = "${pageContext.request.contextPath }";
		    clearInterval(intervalid);
		} else {
		    document.getElementById("time").innerHTML = s;
		    s = s - 1;
		}
    }
</script>
</head>
<body>
	<div>
		<h1>恭喜您激活成功成功！</h1>
		<span>您将在<span id="time">10</span>秒内跳转到首页...
		</span>
		<h2>
			如长时间无反应，请点击<a href="${pageContext.request.contextPath }">这里</a>
		</h2>
	</div>
</body>
</html>

