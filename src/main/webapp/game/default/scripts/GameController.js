GameController = function(gameId,gameName,websocketURL,sessionId){
	var websocket=new WebSocketController();
	var $this=this;
	var players={}; //map of session id to player object; updated by playerList messages from server
	var connected=false;
	var playerRegistered=false;
	var stateListeners=[];
	
	/*send registration message to bind websocket to player*/
	function register(){
		if(websocket){
			websocket.send("game.inbound.GameRegistrationMessage",{
						transactionId:new Date().getTime(),
						userSessionId:SESSION_ID,
						userType:"USER_TYPE_PLAYER"
					});
		}
	};
	function startGame(){
		if(websocket){
			websocket.send("game.inbound.StartGameMessage",{});
		}
	}
	function pauseGame(){
		if(websocket){
			websocket.send("game.inbound.PauseGameMessage",{});
		}
	}
	function resumeGame(){
		if(websocket){
			websocket.send("game.inbound.ResumeGameMessage",{});
		}
	}
	/*
	 * setHeading(Number heading,Number turnDirection);
	 * */
	function setHeading(heading,turnDirection){
		if(turnDirection!=0&&turnDirection!=1){
			turnDirection = 0;
		}
		if(websocket){
			websocket.send("game.defaultgame.inbound.SetHeadingMessage",{
				heading:heading,
				direction:turnDirection
			});
		}
	}
	function setSpeed(speed){
		if(websocket){
			websocket.send("game.defaultgame.inbound.SetSpeedMessage",{
				speed:speed
			});
		}
	}
	function launchMissile(){
		if(websocket){
			websocket.send("game.defaultgame.inbound.LaunchMissileMessage",{});
		}
	}
	function playerListCallback(response){
		for (var i=0,ct=stateListeners.length;i<ct;i++){
			if(stateListeners[i]){
				stateListeners[i](response);
			}
		}
	}
	function registerCallback(response){
		console.log("Registration Successful");
		playerRegistered=true;
	}
	function gameStateCallback(response){
		//console.log("New game state received");
		//console.log(response);
		var newPlayers=response.players;
		var newMissiles=response.missiles;
		$this.gameState.players = newPlayers;
		$this.gameState.missiles = newMissiles;
		for (var i=0,ct=newPlayers.length;i<ct&&newPlayers[i].userSessionId!=sessionId;i++);
		if(i<newPlayers.length){
			$this.player = newPlayers[i];
		}
		for (var i=0,ct=stateListeners.length;i<ct;i++){
			if(stateListeners[i]){
				stateListeners[i]();
			}
		}
	}
	function playerListUpdateCallback(response){
		console.log("Player list updated");
	}
	function gamePausedCallback(response){
		console.log("Game Paused");
	}
	function gameResumedCallback(response){
		console.log("Game Resumed");
	}
	function init(){
		websocket.addEventListener("open",function(){
			logInitMessage("WebSocket established");
			logInitMessage("Sending registration request");
			connected=true;
			register();
		});
		websocket.addEventListener("close",function(){
			connected=false;
		});
		websocket.addEventListener("message",function(data){
			if(typeof(data)=="undefined"){
				console.log("Message received with no data");
			}else{
				console.log("Message Received: "+data);
			}
		});
		websocket.addMessageHandler("game.outbound.RegistrationSuccessMessage",registerCallback);
		websocket.addMessageHandler("game.outbound.PlayerList",playerListCallback);
		websocket.addMessageHandler("game.defaultgame.outbound.GameStateMessage",gameStateCallback);
		websocket.connect(websocketURL);
	};
	this.init = init;
	this.startGame = startGame;
	this.pauseGame = pauseGame;
	this.resumeGame = resumeGame;
	this.setHeading = setHeading;
	this.launchMissile = launchMissile;
	
	this.gameState = {
		players:[],
		missiles:[]
	};
	this.addGameStateListener=function(fn){
		var result=stateListeners.length;
		stateListeners.push(fn);
		return result;
	};
	this.TURN_DIRECTION_CLOCKWISE=0;
	this.TURN_DIRECTION_COUNTERCLOCKWISE=1;
	this.player=null;
};
