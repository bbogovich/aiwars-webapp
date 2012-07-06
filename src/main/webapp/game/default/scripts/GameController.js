GameController = function(gameId,gameName,websocketURL,sessionId){
	var websocket=new WebSocketController();
	var $this=this;
	var connected=false;
	var playerRegistered=false;
	
	/*send registration message to bind websocket to player*/
	function register(){
		if(websocket){
			websocket.send(
					"game.inbound.GameRegistrationMessage",
					{
						transactionId:new Date().getTime(),
						userSessionId:SESSION_ID,
						userType:"USER_TYPE_PLAYER"
					});
		}
	};
	function registerCallback(response){
		console.log("Registration Successful");
		playerRegistered=true;
	};
	this.init=function(){
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
	}
}
