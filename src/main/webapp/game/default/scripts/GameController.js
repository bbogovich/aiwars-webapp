GameController = function(gameId,gameName,websocketURL,sessionId){
	var websocket=new WebSocketController();
	var $this=this;
	
	
	/*send registration message to bind websocket to player*/
	function register(){
		if(websocket){
			websocket.send(
					"game.inbound.GameRegistrationMessage|"+
					JSON.stringify({
							transactionId:new Date().getTime(),
							userSessionId:SESSION_ID,
							userType:"USER_TYPE_PLAYER"
						}));
		}
	};
	this.init=function(){
		websocket.onOpen=function(){
			logInitMessage("WebSocket established");
			logInitMessage("Sending registration request");
			register();
		}
		websocket.connect(websocketURL);
	}
}
