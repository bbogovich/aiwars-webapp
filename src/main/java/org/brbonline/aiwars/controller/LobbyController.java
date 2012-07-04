package org.brbonline.aiwars.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.exception.GameNameAlreadyInUseException;
import org.brbonline.aiwars.game.instance.GameInstance;
import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.model.GameListItem;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LobbyController {
	@Autowired
	private GameManager gameManager;
	
	@RequestMapping(value="/lobby.do", method = RequestMethod.GET)
	public void getLobby(HttpServletRequest request){
		request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
	}

	@RequestMapping(value="/lobby/creategame.do", method = RequestMethod.POST)
	public @ResponseBody String postCreateGame(HttpServletRequest request,HttpServletResponse response){
		String responseBody = "";
		HttpSession session = request.getSession();
		//Set<GameInstance> instances = gameManager.getGameInstances();
		String gameName = request.getParameter("gameName");
		try{
			gameManager.createGameInstance(gameName, gameManager.getUserSessionByHttpSessionId(session.getId()));
			response.setStatus(200);
			responseBody = "{\"message\":\"Game created successfully\"}";
		}catch(GameNameAlreadyInUseException e){
			response.setStatus(500);
			responseBody = "{\"error\":\"Unable to create game.  Name already in use.\"}";
		}
		return responseBody;
	}

	/**
	 * Returns a list of available game instances.  The individual games are identified by
	 * the hash code of the GameInstance
	 * 
	 * @param request
	 * @param model
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/lobby/listgames.do", method = RequestMethod.POST)
	public @ResponseBody String postListGames(HttpServletRequest request, ModelMap model) throws JsonGenerationException, JsonMappingException, IOException{
		Set<GameInstance> instances = gameManager.getGameInstances();
		List<GameListItem> games = new ArrayList<GameListItem>();
		for (GameInstance instance:instances){
			GameListItem item = new GameListItem();
			item.setId(instance.hashCode());
			item.setName(instance.getInstanceName());
			games.add(item);
		}
		model.addAttribute("games",games);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(games);
	}
	
	@RequestMapping(value="/lobby/joingame.do", method = RequestMethod.POST)
	public ModelAndView postJoinGame(HttpServletRequest request,@RequestParam("gameSelection") Integer gameSelection){
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		UserSession userSession = gameManager.getUserSessionByHttpSessionId(session.getId());
		GameInstance game = gameManager.getGameInstanceByHashCode(gameSelection);
		game.addUserToGame(userSession);
		mav.setViewName("redirect:/game/"+game.getGameType()+".do");
		return mav;
	}
}
