UIController = function(gameController){
	var viewerCtx;
	
	function updateViewer(){
		console.log("updateViewer");
		viewerCtx.clearRect(0,0,1000,1000);
		var gameState = gameController.gameState;
		var players = gameState.players;
		for (var i=0,ct=players.length,player;i<ct;i++){
			player=players[i];
			viewerCtx.save();
			viewerCtx.strokeStyle="black";
			viewerCtx.translate(player.positionX,player.positionY);
			viewerCtx.rotate(player.heading);
			viewerCtx.beginPath();
			viewerCtx.moveTo(0,0);
			viewerCtx.lineTo(0,10);
			viewerCtx.stroke();
			viewerCtx.closePath();
			viewerCtx.beginPath();
			viewerCtx.moveTo(0,0);
			viewerCtx.lineTo(-5,-5);
			viewerCtx.stroke();
			viewerCtx.closePath();
			viewerCtx.beginPath();
			viewerCtx.moveTo(0,0);
			viewerCtx.lineTo(5,-5);
			viewerCtx.stroke();
			viewerCtx.closePath();
			viewerCtx.restore();
		}
	}
	
	function initGameStateButtons(){
		var form = document.forms["gameStateButtons"];
		form["start"].addEventListener("click",gameController.startGame,false);
		form["pause"].addEventListener("click",gameController.pauseGame,false);
		form["resume"].addEventListener("click",gameController.resumeGame,false);
	}
	
	function init(){
		initGameStateButtons();
		viewerCtx = document.getElementById("viewer").getContext("2d");
		gameController.addGameStateListener(updateViewer);
	};
	this.init = init;
};
