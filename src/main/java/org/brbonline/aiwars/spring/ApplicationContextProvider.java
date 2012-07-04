package org.brbonline.aiwars.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
 
/**
 * Provide access to the Spring application context from non-spring managed objects
 */
public class ApplicationContextProvider implements ApplicationContextAware { 
	private static ApplicationContext applicationContext;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 * This method must be on an instance so that Spring can create one of its
	 * obnoxious singletons.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException { 
		ApplicationContextProvider.applicationContext=applicationContext; 
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
}