UIController = function(gameController){
	var viewerCtx=null;
	var UNIVERSE_WIDTH=500;
	var UNIVERSE_HEIGHT=500;
	
	var playerColors=["red","blue","green"];
	var intFrameTimeout=null;
	
	function drawPlayer(player,color){
		viewerCtx.save();
		viewerCtx.strokeStyle=color;
		viewerCtx.translate(player.positionX,player.positionY);
		viewerCtx.rotate(player.heading);
		viewerCtx.beginPath();
		viewerCtx.moveTo(0,0);
		viewerCtx.lineTo(10,0);
		viewerCtx.moveTo(0,0);
		viewerCtx.lineTo(-5,-5);
		viewerCtx.moveTo(0,0);
		viewerCtx.lineTo(-5,5);
		viewerCtx.stroke();
		viewerCtx.closePath();
		viewerCtx.restore();
	}
	
	function drawMissile(missile){
		//console.log("drawMissile "+missile);
		var rotation = Math.random()*Math.PI;
		viewerCtx.save();
		viewerCtx.strokeStyle="red";
		viewerCtx.translate(missile.positionX,missile.positionY);
		viewerCtx.rotate(rotation);
		viewerCtx.beginPath();
		viewerCtx.moveTo(0,-5);
		viewerCtx.lineTo(0,5);
		viewerCtx.moveTo(-5,0);
		viewerCtx.lineTo(5,0);
		viewerCtx.moveTo(-10,-10);
		viewerCtx.lineTo(10,10);
		viewerCtx.moveTo(-8,8);
		viewerCtx.lineTo(8,-8);
		viewerCtx.stroke();
		viewerCtx.closePath();
		viewerCtx.restore();
	}
	
	function drawView(){
		if(intFrameTimeout){
			window.clearTimeout(intFrameTimeout);
		}
		viewerCtx.clearRect(0,0,UNIVERSE_WIDTH,UNIVERSE_HEIGHT);
		var gameState = gameController.gameState;
		var players = gameState.players;
		var missiles = gameState.missiles;
		//player is an arrow pointing right at zero degrees
		//rotation is counter clockwise since vector calculations assume a counter-clockwise unit circle
		for (var i=0,ct=players.length;i<ct;i++){
			drawPlayer(players[i],playerColors[i%3]);
		}
		if(missiles){
			for (i=0,ct=missiles.length;i<ct;i++){
				drawMissile(missiles[i]);
			}
		}
	}
	
	function drawInterpolatedFrame(){
		var gameState = gameController.gameState;
		var players = gameState.players;
		for (var i=0,ct=players.length;i<ct;i++){
			var player = players[i];
			player.positionX = player.positionX + player.velocityX;
			player.positionY = player.positionY + player.velocityY;
		}
		var missiles = gameState.missiles;
		if(missiles){
			for (var i=0,ct=missiles.length;i<ct;i++){
				var missile = missiles[i];
				if(missile){
					missile.positionX = missile.positionX + missile.velocityX;
					missile.positionY = missile.positionY + missile.velocityY;
				}
			}
		}
		drawView();
		intFrameTimeout = window.setTimeout(drawInterpolatedFrame,20);
	}
	
	function updateViewer(){
		console.log("updateViewer");
		if(intFrameTimeout){
			window.clearTimeout(intFrameTimeout);
		}
		var gameState = gameController.gameState;
		var players = gameState.players;
		//player is an arrow pointing right at zero degrees
		//rotation is counter clockwise since vector calculations assume a counter-clockwise unit circle
		for (var i=0,ct=players.length;i<ct;i++){
			var player=players[i];
			player.lastUpdateX=player.positionX;
			player.lastUpdateY = player.positionY;
		}
		drawView();
		intFrameTimeout = window.setTimeout(drawInterpolatedFrame,20);
	}
	
	function turnLeftButtonHandler(){
		var player = gameController.player;
		if(player){
			var newHeading = player.heading-0.2;
			if(newHeading<0){
				newHeading = Math.PI*2-newHeading;
			}
			gameController.setHeading(newHeading,gameController.TURN_DIRECTION_COUNTERCLOCKWISE);
		}
	}
	
	function turnRightButtonHandler(){
		var player = gameController.player;
		if(player){
			var newHeading = player.heading+0.2;
			if(newHeading>Math.PI*2){
				newHeading = newHeading-Math.PI*2;
			}
			gameController.setHeading(newHeading,gameController.TURN_DIRECTION_CLOCKWISE);
		}
	}
	var inputStatus = {
		turnLeftButtonPressed:false,
		turnRightButtonPressed:false
	};
	
	function initGameStateButtons(){
		var form = document.forms["gameStateButtons"];
		form["start"].addEventListener("click",gameController.startGame,false);
		form["pause"].addEventListener("click",gameController.pauseGame,false);
		form["resume"].addEventListener("click",gameController.resumeGame,false);
	}
	
	function initControlButtons(){
		var form = document.forms["steeringButtons"];
		form["turnLeft"].addEventListener("click",turnLeftButtonHandler,false);
		form["turnLeft"].addEventListener("mousedown",function(){
			inputStatus.turnLeftButtonPressed=true;
		},false);
		form["turnLeft"].addEventListener("mouseup",function(){
			inputStatus.turnLeftButtonPressed=false;
		},false);
		form["turnLeft"].addEventListener("mouseout",function(){
			inputStatus.turnLeftButtonPressed=false;
		},false);
		form["turnRight"].addEventListener("click",turnRightButtonHandler,false);
		form["turnRight"].addEventListener("mousedown",function(){
			inputStatus.turnRightButtonPressed=true;
		},false);
		form["turnRight"].addEventListener("mouseup",function(){
			inputStatus.turnRightButtonPressed=false;
		},false);
		form["turnRight"].addEventListener("mouseout",function(){
			inputStatus.turnRightButtonPressed=false;
		},false);
		form["launchMissile"].addEventListener("click",gameController.launchMissile);
	}
	
	
	function clearMouseButtonStatus(){
		for (var i in inputStatus){
			inputStatus[i]=false;
		}
	}
	
	function processInput(){
		if(inputStatus.turnLeftButtonPressed){
			turnLeftButtonHandler();
		} else if(inputStatus.turnRightButtonPressed){
			turnRightButtonHandler();
		}
		window.setTimeout(processInput,400);
	}
	
	function init(){
		initGameStateButtons();
		initControlButtons();
		viewerCtx = document.getElementById("viewer").getContext("2d");
		gameController.addGameStateListener(updateViewer);
		window.setTimeout(processInput,250);
	};
	this.init = init;
};
