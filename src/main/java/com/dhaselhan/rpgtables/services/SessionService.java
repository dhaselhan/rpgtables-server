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
	
	public boolean registerSession(String token, User user, Date expiryDate) {
		UserSession session = new UserSession();
		session.setToken(token);
		session.setUserName(user.getUsername());
		session.setExpiryDate(expiryDate);
		
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.merge(session);
		trans.commit();
		em.close();
		return true;
	}
	
	public boolean isTokenValid(String token, String userName) {
		EntityManager em = factory.createEntityManager();
		UserSession result = em.find(UserSession.class, userName);
		
		if (result != null && result.getExpiryDate().before(new Date())) {
			return true;
		}
		
		return false;
	}
}
