<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title><spring:message code="login.page.title"/></title>
		<link rel="stylesheet" type="text/css" href="../styles/global.css"/>
		<link rel="stylesheet" type="text/css" href="../styles/lobby.css"/>
		<script>
var SOCKET_PORT=${socketPort};
var WEBSOCKET_PORT=${websocketPort};
var GAME_ID=${gameId};
var GAME_NAME="${gameName}";
var SESSION_ID="${sessionId}";



function logInitMessage(msg){
	var ele = document.createElement("div");
	ele.appendChild(document.createTextNode(msg));
	document.getElementById("initStatus").appendChild(ele);
}
var gameController;

function init(){
	//initialize websocket for viewer
	logInitMessage("starting client initialization");
	logInitMessage("SessionId: "+SESSION_ID);
	logInitMessage("opening websocket connection");
}
		</script>
		<link rel="stylesheet" href="global.css" type="text/css"/>
	</HEAD>
	<body onload="init()">
		<div id="initDialog">
			<div>Initializing....</div>
			<div id="initStatus"></div>
		</div>
		<div class="main" style="display:none">
			<div id="viewportWrapper">
				<canvas id="viewer" width="500px" height="500px"></canvas>
			</div>
			<div id="gameInfo">
				<div id="playerList"></div>
			</div>
			<div class="infoPanel"></div>
		</div>
	</body>
</html>