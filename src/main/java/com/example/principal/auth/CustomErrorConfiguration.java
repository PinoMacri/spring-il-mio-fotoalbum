package com.example.principal.auth;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class CustomErrorConfiguration {
	
	    public void configureViewResolvers(ViewResolverRegistry registry) {
	        registry.viewResolver(new InternalResourceViewResolver());
	    }


	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/error").setViewName("error");
	    }
}
