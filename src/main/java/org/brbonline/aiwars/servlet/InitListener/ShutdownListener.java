package org.brbonline.aiwars.servlet.InitListener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.brbonline.aiwars.contextmanager.DefaultGameManager;
import org.brbonline.aiwars.spring.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

/**
 * Cleanup listener.  Shuts down socket servers.
 */
public class ShutdownListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		/*
		ApplicationContext springContext = ApplicationContextProvider.getApplicationContext();
		GameManager gameManager = springContext.getBean(GameManager.class);
		if(gameManager!=null){
			gameManager.shutdown();
		}
		*/
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}
}
