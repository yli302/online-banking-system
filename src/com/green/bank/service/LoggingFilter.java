package com.green.bank.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

@WebFilter(urlPatterns = "*")
public class LoggingFilter implements Filter {
	Logger logger = Logger.getLogger(LoggingFilter.class);
//	BasicConfigurator.configure();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
//		logger.info("Request comming for url: " + req.getRequestURI());
		chain.doFilter(request, response); // go ahead
//		logger.info("Response sent for url: " + req.getRequestURL());
	}

}
