package com.dhaselhan.rpgtables.security;

import java.io.IOException;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.dhaselhan.rpgtables.model.UserSession;
import com.dhaselhan.rpgtables.services.SessionService;

@Provider
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter,
		ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request,
			ContainerResponseContext response) throws IOException {
		String tokenValue = request.getHeaderString("Authorization");
		if (tokenValue != null) {
			UserSession session = SessionService.getSessionService().isTokenValid(tokenValue);
			if (session != null) {
				return;
			}
		}
		throw new ForbiddenException("This resource requires authentication");
	}

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		String tokenValue = request.getHeaderString("Authorization");
		if (tokenValue != null) {
			UserSession session = SessionService.getSessionService().isTokenValid(tokenValue);
			if (session != null) {
				String userName = session.getUserName();
				request.setProperty("userName", userName);
				return;
			}
		}
		throw new ForbiddenException("This resource requires authentication");
	}
}