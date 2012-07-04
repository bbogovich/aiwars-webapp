<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title><spring:message code="login.page.title"/></title>
		<script type="text/javascript">
function init(){
	document.forms[0]["j_username"].focus();
}
function onLoginFormSubmit(){
	document.getElementById("loginFormWrapper").style.display="none";
	document.getElementById("wait").style.display="block";
}
		</script>
		<link rel="stylesheet" href="styles/global.css" type="text/css"/>
		<link rel="stylesheet" href="styles/login.css" type="text/css"/>
	</HEAD>
	<body onload="init()">
		<div class="main">
			<h1><spring:message code="login.heading.title"/></h1>
			<h3 id="wait"><spring:message code="login.wait.message"/></h3>
			<div id="loginFormWrapper">
				<c:if test="${!empty errors}">
					<div class="error">
						<h2><spring:message code="login.error.title"/></h2>
						<ul>
							<c:forEach items="${errors}" var="err" varStatus="status">
								<li><spring:message code="${err}"/></li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<form action="j_spring_security_check" method="post">
					<table border="0">
						<tbody>
							<tr>
								<th>
									<label for="j_username"><spring:message code="login.username.label"/></label>
								</th>
								<td>
									<input type="text" name="j_username" id="j_username"/>
								</td>
							</tr>
							<tr>
								<th>
									<label for="j_password"><spring:message code="login.password.label"/></label>
								</th>
								<td width="313">
									<input type="password" name="j_password" id="j_password"/>
								</td>
							</tr>
							<!--
							<tr>
								<td colspan="2">
									<input type="checkbox" name="_spring_security_remember_me"/> Remember me on this computer.
								</td>
							</tr>-->
							<tr>
								<td></td>
								<td>
									<button class="login" type="submit" value="login"><div><spring:message code="login.submit"/></div></button>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
				<p>This will be some really awesome description text!  Stay tuned!</p>
			</div>
		</div>
	</body>
</html>