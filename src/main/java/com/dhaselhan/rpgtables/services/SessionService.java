package com.dhaselhan.rpgtables.services;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.dhaselhan.rpgtables.model.User;
import com.dhaselhan.rpgtables.model.UserSession;

public class SessionService {

	private final EntityManagerFactory factory;
	
	private static SessionService sessionService;
	
	private SessionService() {
		factory = Persistence.createEntityManagerFactory(AppConstants.DATABASE_NAME);
	}
	

	public UserSession findSession(String token) {
		EntityManager em = factory.createEntityManager();
		return em.find(UserSession.class, token);
	}

	public static SessionService getSessionService() {
		if (sessionService == null) {
			sessionService = new SessionService();
		}

		return sessionService;
	}
	
	public boolean registerSession(String token, User user, Date expiryDate) {
		UserSession userSession = findSession(token);
		if (userSession == null) {
			userSession = new UserSession();
			userSession.setUserName(user.getUsername());
		}
		userSession.setToken(token);
		userSession.setExpiryDate(expiryDate);
		
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.merge(userSession);
		trans.commit();
		em.close();
		return true;
	}

	public UserSession isTokenValid(String token) {
		EntityManager em = factory.createEntityManager();
		UserSession result = em.find(UserSession.class, token);

		if (result != null && result.getExpiryDate().after(new Date())) {
			return result;
		}

		return null;
	}
}
