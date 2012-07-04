package org.brbonline.aiwars.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.game.instance.GameInstance;
import org.brbonline.aiwars.game.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameWindowController {
	@Autowired
	private GameManager gameManager;

	@RequestMapping(value="/game/{gameType}.do",method=RequestMethod.GET)
	public ModelAndView getGameScreen(HttpServletRequest request,@PathVariable String gameType){
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		UserSession userSession = gameManager.getUserSessionByHttpSessionId(session.getId());
		GameInstance activeGame = userSession.getActiveGame();
		mav.addObject("sessionId",session.getId());
		mav.addObject("gameName",activeGame.getInstanceName());
		mav.addObject("gameId",activeGame.hashCode());
		mav.addObject("websocketPort",gameManager.getWebsocketPort());
		mav.addObject("socketPort",gameManager.getSocketPort());
		return mav;
	}
	
}
