UIController = function(gameController){
	var viewerCtx=null;
	var UNIVERSE_WIDTH=500;
	var UNIVERSE_HEIGHT=500;
	
	function updateViewer(){
		console.log("updateViewer");
		viewerCtx.clearRect(0,0,UNIVERSE_WIDTH,UNIVERSE_HEIGHT);
		var gameState = gameController.gameState;
		var players = gameState.players;
		//player is an arrow pointing right at zero degrees
		//rotation is counter clockwise since vector calculations assume a counter-clockwise unit circle
		for (var i=0,ct=players.length,player;i<ct;i++){
			player=players[i];
			viewerCtx.save();
			viewerCtx.strokeStyle="black";
			viewerCtx.translate(player.positionX,UNIVERSE_HEIGHT-player.positionY);  //canvas orientation is flipped vertically from world used for vector calculations.  subtract yPos from height to compensate.
			viewerCtx.rotate(player.heading);  //canvas uses y axis (negative) as zero degrees orientation.  subtract pi/2 from heading to compensate.
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
	}
	
	function turnLeftButtonHandler(){
		var player = gameController.player;
		if(player){
			var newHeading = player.heading+0.2;
			if(newHeading>Math.PI*2){
				newHeading = newHeading-Math.PI*2;
			}
			gameController.setHeading(newHeading,gameController.TURN_DIRECTION_CLOCKWISE);
		}
		
	}
	function turnRightButtonHandler(){
		var player = gameController.player;
		if(player){
			var newHeading = player.heading-0.2;
			if(newHeading<0){
				newHeading = Math.PI*2-newHeading;
			}
			gameController.setHeading(newHeading,gameController.TURN_DIRECTION_COUNTERCLOCKWISE);
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
		//window.setTimeout(processInput,250);
	};
	this.init = init;
};
