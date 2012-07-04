WebSocketController = function(){
	var $this=this;
	var websocket=null;
	var messageHandlers={};
	var websocketURL="";
	
	function onMessage(/*String*/data){
		console.log(data);
		try{
			var message = JSON.parse(data);
			var messageType = message.messageType;
			var messageHandler = messageHandlers[messageType];
			if(typeof(messageHandler)=="function"){
				messageHandler(message);
			}else{
				console.warn("Unregistered message type "+message.messageType);
			}
		} catch(e) {
			console.warn("Unable to parse incoming message - "+data+" -- "+e.message);
		}
	}
	function openWebSocket(){
		if(!window.WebSocket){
			console.log("native websockets are not available.");
			if(window.MozWebSocket){
				window.WebSocket = window.MozWebSocket;
				openWebSocket();
				return;
			}else{
				console.log("load the flash implementation");
				window.WEB_SOCKET_SWF_LOCATION = "WebSocketMain.swf";
				window.WEB_SOCKET_DEBUG = true;
				loadScript("swfobject.js",function(){
					console.log("swfobject.js loaded")
					loadScript("FABridge.js",function(){
						console.log("FABridge.js loaded")
						loadScript("web_socket.js",function(){
							console.log("web_socket.js loaded");
							WebSocket.__initialize();
							setTimeout(openWebSocket,100);
						});
					});
				});
				console.log("waiting for dependencies");
			}
		}else{
			if(!websocket){
				console.log("Instantiate new WebSocket object");
				websocket = new WebSocket(websocketURL);
				$this.websocket=websocket;
				websocket.onopen = function(e){
					websocketOpen=true;
					console.log("websocket.onopen");
					if($this.onOpen){
						$this.onOpen();
					}
				}
				websocket.onmessage = function(e){
					console.log("websocket.onmessage");
					console.log(e);
					onMessage(e.data);
				}
				websocket.onclose=function(e){
					websocket=null;
					if($this.onClose){
						$this.onClose();
					}
				}
				console.log(websocket);
			}
		}
	}
	function closeWebSocket(){
		if(websocket){
			websocket.close();
		}
	}
	var messageHandlers={
		"game.outbound.RegistrationSuccessResponse":function(/*Object*/ message){
			console.log("Registration Successful");
		}
	};
	/*
	 *  connect(String url)
	 *  Opens a websocket connection to the specified URL.
	 *  Typically the URL is formated ws://<hostname>:<port>
	 */
	this.connect=function(/*String*/ url){
		websocketURL=url;
		openWebSocket();
	};
	/*
	 * disconnect()
	 * Closes the websocket connection.
	 */
	this.disconnect = function(){
		closeWebSocket();
	};
	this.send=function(/*String*/messageType,/*Object*/data){
		if(!websocket){
			alert("websocket is not yet open");
		}else{
			websocket.send(messageType+"|"+JSON.stringify(data));
		}
	};
	this.addMessageHandler = function(/*String*/messageType,/*function(message)*/callback){
		console.log("Registering message handler of type "+messageType);
		if(!messageHandlers[messageType]){
			messageHandlers[messageType] = [];
		}
		messageHandlers[messageType].push(callback);
	}
	
};
