<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title><spring:message code="login.page.title"/></title>
		<link rel="stylesheet" type="text/css" href="../styles/global.css"/>
		<script src="default/scripts/WebSocketController.js"></script>
		<script src="default/scripts/GameController.js"></script>
		<script src="default/scripts/UIController.js"></script>
		<script>
var SOCKET_PORT=${socketPort};
var WEBSOCKET_PORT=${websocketPort};
var WEBSOCKET_URL="ws://"+document.location.hostname+":"+WEBSOCKET_PORT;
var GAME_ID=${gameId};
var GAME_NAME="${gameName}";
var SESSION_ID="${sessionId}";
var gameController;
var uiController;

function logInitMessage(msg){
	var ele = document.createElement("div");
	ele.appendChild(document.createTextNode(msg));
	document.getElementById("initStatus").appendChild(ele);
}

function init(){
	//initialize websocket for viewer
	logInitMessage("starting client initialization");
	logInitMessage("SessionId: "+SESSION_ID);
	gameController = new GameController(GAME_ID,GAME_NAME,WEBSOCKET_URL,SESSION_ID);
	uiController = new UIController(gameController);
	logInitMessage("opening websocket connection");
	gameController.init();
	uiController.init();
}
		</script>
		<style>
#viewer{
	border: 2px black solid;
}
		</style>
	</HEAD>
	<body onload="init()">
		<div class="main">
			<div id="viewportWrapper">
				<canvas id="viewer" width="500px" height="500px"></canvas>
			</div>
			<div id="gameInfo">
				<div id="playerList"></div>
			</div>
			<div class="infoPanel">
				<table>
					<tbody>
						<tr>
							<th>Heading:</th>
							<td><span id="info_heading"></span></td>
						</tr>
						<tr>
							<th>Speed:</th>
							<td><span id="info_speed"></span></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="steering">
				<form name="steeringButtons">
					<button name="turnLeft">&lt;</button>
					<button name="turnRight">&gt;</button>
				</form>
			</div>
			<div class="buttons">
				<form name="gameStateButtons">
					<button name="start" type="button">Start Game</button>
					<button name="pause" type="button">Pause Game</button>
					<button name="resume" type="button">Resume Game</button>
				</form>
			</div>
		</div>
		<div id="initDialog">
			<div>Initializing....</div>
			<div id="initStatus"></div>
		</div>
	</body>
</html>