<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title><spring:message code="login.page.title"/></title>
		<link rel="stylesheet" type="text/css" href="styles/global.css"/>
		<link rel="stylesheet" type="text/css" href="styles/lobby.css"/>
		<script type="text/javascript">
function encodeForm(form){
	var result=null;
	if(typeof(form)=="String"){
		form = document.forms[formName];
	}
	if(form){
		var values = {};
		var formEles = form.elements;
		for (var i=0,ct=formEles.length;i<ct;i++){
			var ele = formEles[i];
			var tagName = ele.tagName.toLowerCase();
			if(tagName=="input"){
				var type = ele.getAttribute("type");
				if(type=="text"||type=="hidden"||type=="password"||type=="email"){
					values[ele.getAttribute("name")] = ele.value;
				}else if ((type=="radio")&&ele.checked){
					values[ele.getAttribute("name")] = ele.value;
				}else if (type=="checkbox"&&ele.checked){
					values[ele.getAttribute("name")] = true;
				}
			}else{
				if(tagName=="select"||tagName=="textarea"){
					values[ele.getAttribute("name")] = ele.value;
				}
			}
		}
		var requestAttributes = [];
		var spaces = /%20/g;
		for(var name in values) {
			requestAttributes.push(
				encodeURIComponent(name).replace(spaces,"+") + '=' +
				encodeURIComponent(values[name].toString()).replace(spaces,"+")
			);
		}
		result = requestAttributes.join('&');
	}
	return result;
}

function onCreateGame(){
	var request = new XMLHttpRequest();
	var gameName = document.forms["createGameForm"]["gameName"].value;
	request.open("POST", "./lobby/creategame.do", true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	request.setRequestHeader("Accept","application/json");
	request.send("gameName="+encodeURIComponent(gameName).replace(/%20/g,"+"));
	request.onreadystatechange=function(){
		if(request.readyState==4){
			console.log("game created");
		}
	}
}

function loadGameList(){
	var request = new XMLHttpRequest();
	request.open("POST", "./lobby/listgames.do", true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	request.setRequestHeader("Accept","application/json");
	request.send();
	request.onreadystatechange=function(){
		if(request.readyState==4){
			console.log(request.responseText);
			
			var gameSelector = document.getElementById("gameSelection");
			var options=gameSelector.options;
			var gameList = JSON.parse(request.responseText);
			//remove any deleted games
			var gameMap={};
			for (var i=0,ct=gameList.length;i<ct;i++){
				var game = gameList[i];
				gameMap[game.id]=1;
			}
			var optsToRemove=[];
			for (i=0;i<options.length;i++){
				var option = options[i];
				if(!gameMap[option.value]){
					optsToRemove.push(option);
				}
			}
			while(optsToRemove.length){
				var option = optsToRemove.pop();
				option.parentNode.removeChild(option);
			}
			sessionMap=null;
			//add new users
			for (var i=0,ct=gameList.length;i<ct;i++){
				var game=gameList[i];
				var gameId=game["id"];
				console.log("looking for game with id "+gameId);
				for (var j=0,found=false,opts=gameSelector.options,ct2=opts.length;j<ct2&&!found;j++){
					found=opts[j].value==gameId;
				}
				if(found){
					opts[j-1].text=game.name;
				}else{
					var opt = document.createElement("option");
					opt.value=gameId;
					opt.text=game.name;
					gameSelector.add(opt);
				}
			}
		}
	}
	setTimeout(loadGameList,5000);
}
function init(){
	loadGameList();
}
		</script>
	</HEAD>
	<body onload="init()">
		<div class="main">
			<h1><spring:message code="welcome.heading.title"/></h1>
			<div id="welcomePanel">
				<div class="left">
					Welcome, ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}!
				</div>
				<div class="right">
					<form action="logout.do">
						<button type="submit">Log Out</button>
					</form>
				</div>
			</div>
			<div id="infoPanel">
				<h2></h2>
				<p>
				Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore 
				et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut 
				aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum 
				dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui 
				officia deserunt mollit anim id est laborum.
				</p>
			</div>
			<div id="gameListWrapper">
				<div class="gameList">
					<h2>Game List</h2>
					<form name="gameSelectionForm" method="post" action="./lobby/joingame.do">
						<select id="gameSelection" name="gameSelection" size="20"></select>
						<div><button type="submit">Join Game</button></div>
					</form>
				</div>
				<div class="createGameForm">
					<h2>Create New Game</h2>
					<form name="createGameForm" onsubmit="return false">
						<input type="text" name="gameName"/><button type="button" onclick="onCreateGame()">Create Game</button>
						<select name="gameType">
							<option value="DefaultGameInstance">Default</option>
						</select>
					</form>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</body>
</html>