GameController = function(gameId,gameName,websocketURL,sessionId){
	var websocket=new WebSocketController();
	var $this=this;
	var connected=false;
	var playerRegistered=false;
	
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
	function registerCallback(response){
		console.log("Registration Successful");
		playerRegistered=true;
	}
	function gameStateCallback(response){
		console.log("New game state received");
		console.log(response);
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
			console.log("Message Received: "+data);
		});
		websocket.addMessageHandler("org.brbonline.aiwars.socketprotocol.game.outbound.RegistrationSuccessMessage",registerCallback);
		websocket.connect(websocketURL);
	};
	this.init = init;
	this.startGame = startGame;
	this.pauseGame = pauseGame;
};
