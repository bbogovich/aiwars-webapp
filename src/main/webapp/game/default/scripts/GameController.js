GameController = function(gameId,gameName,websocketURL,sessionId){
	var websocket=new WebSocketController();
	var $this=this;
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
			websocket.send("game.inbound.StartGameMessage",{transactionId:new Date().getTime()});
		}
	}
	function pauseGame(){
		if(websocket){
			websocket.send("game.inbound.PauseGameMessage",{transactionId:new Date().getTime()});
		}
	}
	function resumeGame(){
		if(websocket){
			websocket.send("game.inbound.PauseGameMessage",{transactionId:new Date().getTime()});
		}
	}
	function registerCallback(response){
		console.log("Registration Successful");
		playerRegistered=true;
	}
	function gameStateCallback(response){
		console.log("New game state received");
		console.log(response);
		$this.gameState.players = response.players;
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
		websocket.addMessageHandler("game.defaultgame.outbound.GameStateMessage",gameStateCallback);
		websocket.connect(websocketURL);
	};
	this.init = init;
	this.startGame = startGame;
	this.pauseGame = pauseGame;
	this.gameState = {
		players:[],
		missiles:[]
	};
	this.addGameStateListener=function(fn){
		var result=stateListeners.length;
		stateListeners.push(fn);
		return result;
	};
};
