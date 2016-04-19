package com.woniu.base.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	private static ApplicationContext ctx = null;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		ApplicationContextProvider.ctx = ctx;
	}
}
