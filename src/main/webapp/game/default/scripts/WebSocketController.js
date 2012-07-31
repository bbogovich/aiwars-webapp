WebSocketController = function(){
	var $this=this;
	var websocket=null;
	var eventListeners={
		open:[],
		close:[],
		message:[]
	};
	var messageHandlers={};
	var websocketURL="ws://localhost:1234";
	
	function onOpen(){
		var listeners=eventListeners["open"];
		for (var i=0,ct=listeners.length;i<ct;i++){
			if(listeners[i]){
				listeners[i]();
			}
		}
	}
	
	function onClose(){
		var listeners=eventListeners["close"];
		for (var i=0,ct=listeners.length;i<ct;i++){
			if(listeners[i]){
				listeners[i]();
			}
		}
	}
	
	function onMessage(/*String*/data){
		console.log(data);
		var listeners=eventListeners["message"];
		for (var i=0,ct=listeners.length;i<ct;i++){
			if(listeners[i]){
				listeners[i]();
			}
		}
		try{
			console.log("Parse incoming message");
			var message = JSON.parse(data);
			console.log(message);
			var messageType = message.messageType;
			console.log("message type: "+messageType);
			var messageHandler = messageHandlers[messageType];
			if(messageHandler){
				for (var i=0,ct=messageHandler.length;i<ct;i++){
					var handler = messageHandler[i];
					if(typeof(handler)=="function"){
						handler(message);
					}else{
						console.warn("Unregistered message type "+message.messageType);
					}
				}
			}
		} catch(e) {
			console.warn("Unable to parse incoming message - "+data+" -- "+e.message);
		}
	}
	
	function openWebSocket(){
		if(!websocket){
			console.log("Instantiate new WebSocket object");
			websocket = new WebSocket(websocketURL);
			$this.websocket=websocket;
			websocket.onopen = function(e){
				websocketOpen=true;
				console.log("websocket.onopen");
				onOpen();
			};
			websocket.onmessage = function(e){
				console.log("websocket.onmessage");
				console.log(e);
				onMessage(e.data);
			};
			websocket.onclose=function(e){
				websocket=null;
				onClose();
			};
			console.log(websocket);
		}
	}
	
	function closeWebSocket(){
		if(websocket){
			websocket.close();
			websocket=null;
		}
	}
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
	this.write=function(/*<String|Number|Boolean>*/data){
		if(!websocket){
			alert("websocket is not yet open");
		}else{
			websocket.send(data);
		}
	};
	this.send=function(/*String*/messageType,/*Object*/data){
		if(!websocket){
			alert("websocket is not yet open");
		}else{
			var message = messageType+"|"+JSON.stringify(data);
			console.log("Send:\n"+message);
			websocket.send(message);
		}
	};
	this.addEventListener = function(eventType/*[<String> messageType],<Function> callback*/){
		var eventId=-1;
		var listeners;
		switch(eventType){
			case "open":
				listeners = eventListeners["open"];
				eventId = listeners.length;
				listeners.push(arguments[1]);
				break;
			case "close":
				listeners = eventListeners["close"];
				eventId = listeners.length;
				listeners.push(arguments[1]);
				break;
			case "message":
				if(arguments.length==2){ //this event is called for all messages received
					listeners = eventListeners["message"];
					eventId = listeners.length;
					listeners.push(arguments[1]);
				}else{
					eventId = $this.addMessageHandler(arguments[1],arguments[2]);
				}
				break;
			default:
				console.error("Unknown event listener "+eventTYpe);
		}
		return eventId;
	};
	this.removeEventListener = function(eventType/*,[<String> messageType,]<Number> index*/){
		switch(eventType){
			case "open":
				eventListeners["open"][arguments[1]]=null;
				break;
			case "disconnect":
				eventListeners["close"][arguments[1]]=null;
				break;
			case "message":
				if(arguments.length==2){ //this event is called for all messages received
					eventListeners["message"][arguments[1]]=null;
				}else{
					eventId = $this.removeMessageHandler(arguments[1],arguments[2]);
				}
				break;
			default:
				console.error("Unknown event listener "+eventTYpe);
		}
		return eventId;
	};
	this.addMessageHandler = function(/*String*/messageType,/*function(message)*/callback){
		console.log("Registering message handler of type "+messageType);
		var idx=0;
		if(!messageHandlers[messageType]){
			messageHandlers[messageType] = [];
		}else{
			idx=messageHandlers.length;
		}
		messageHandlers[messageType].push(callback);
		return idx;
	};
	this.removeMessageHandler=function(/*<String>*/messageType,/*<Number>*/idx){
		if(messageHandlers[messageType]){
			messageHanlders[messageType][idx]=null;
		}else{
			console.error("Attempting to remove an unknown message handler type \""+messageType+"\"");
		}
	};
	if(!window.WebSocket){
		if(window.MozWebSocket){
			window.WebSocket = window.MozWebSocket;
		}else{
			console.error("Native websockets are not available.");
		}
	}
};
