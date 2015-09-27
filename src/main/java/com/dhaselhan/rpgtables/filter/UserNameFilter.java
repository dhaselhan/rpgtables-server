package com.dhaselhan.rpgtables.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.dhaselhan.rpgtables.data.UserSession;
import com.dhaselhan.rpgtables.services.SessionService;

public class UserNameFilter implements Filter {
	
	public static final String USERNAME = "userName";

	private SessionService sessionService;

	@Override
	public void destroy() {
		// Do Nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String headerValue = httpRequest.getHeader("Authorization");
			if (headerValue != null) {
				String tokenValue = headerValue.split(" ")[1];
				UserSession session = sessionService.isTokenValid(tokenValue);
				if (session != null) {
					String userName = session.getUserName();
					request.setAttribute(USERNAME, userName);
					filterChain.doFilter(request, response);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		sessionService = new SessionService();
	}
}