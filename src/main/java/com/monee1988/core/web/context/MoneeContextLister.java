package com.monee1988.core.web.context;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.monee1988.core.util.Global;

public class MoneeContextLister extends ContextLoaderListener {
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {

		StringBuilder builder = new StringBuilder();
		
		builder.append("============================================================================\n\n");
		builder.append(" Welcome to use MoneePlatFrom  Copyright © 2015-2016 "+	Global.getCompanyName()+"  All Rights Reserved\n\n");
		builder.append("============================================================================");
		System.out.println(builder);
		
		return super.initWebApplicationContext(servletContext);
	}
}
