package org.brbonline.aiwars.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.brbonline.aiwars.contextmanager.GameManager;
import org.brbonline.aiwars.dao.UserAccountDao;
import org.brbonline.aiwars.game.user.UserSession;
import org.brbonline.aiwars.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;

@Controller
public class LoginController {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private GameManager gameManager;
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public void getLogin(HttpServletRequest request) {
		logger.info("LoginController.getLogin()");
		HttpSession session = request.getSession();
		Exception lastException = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		// SavedRequest springSavedRequest = (SavedRequest)session.getAttribute("SPRING_SECURITY_SAVED_REQUEST" );
		if (lastException != null) {
			if (lastException.getClass().equals(BadCredentialsException.class)) {
				logger.info("BadCredentialsException found");
			}
		}
		// don't do anything, all it's doing is redirecting to the login jsp.
		// the view name is detected automatically
		// as "login"
	}
	
	@RequestMapping(value = "/postlogin.do", method = RequestMethod.GET)
	public ModelAndView postLogin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User principal = (User)(securityContext.getAuthentication().getPrincipal());
		String username = principal.getUsername();
		session.setAttribute("User",username);
		logger.info("PostLogin for user "+username+" with session id "+session.getId());
		UserSession userSession = new UserSession();
		UserAccount userAccount = userAccountDao.findByUsername(username);
		userSession.setUserAccount(userAccount);
		userSession.setHttpSessionId(session.getId());
		userSession.setLastActivity(new Date());
		gameManager.addUserSession(userSession);
		mav.setViewName("redirect:/lobby.do");
		return mav;
	}
	
}