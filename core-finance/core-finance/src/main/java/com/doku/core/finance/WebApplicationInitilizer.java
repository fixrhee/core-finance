package com.doku.core.finance;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebApplicationInitilizer implements WebApplicationInitializer {

	@Override
	@SuppressWarnings("resource")
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(ServiceConfiguration.class);
	}

}
