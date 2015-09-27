package com.dhaselhan.rpgtables.services;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.dhaselhan.rpgtables.data.User;
import com.dhaselhan.rpgtables.data.UserSession;

public class SessionService {

	private EntityManagerFactory factory;
	
	public SessionService() {
		factory = Persistence.createEntityManagerFactory(AppConstants.TABLE_NAME);
	}
	
	public UserSession findSession(String token) {
		EntityManager em = factory.createEntityManager();
		return em.find(UserSession.class, token);
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
