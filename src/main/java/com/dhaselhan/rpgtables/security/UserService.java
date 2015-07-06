package com.dhaselhan.rpgtables.security;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dhaselhan.rpgtables.data.DataTable;
import com.dhaselhan.rpgtables.data.User;
import com.dhaselhan.rpgtables.services.AppConstants;
import com.dhaselhan.rpgtables.services.DataTablePersistenceService;

public class UserService {

	private EntityManagerFactory factory;

	private DataTablePersistenceService tableService;

	public UserService() {
		factory = Persistence.createEntityManagerFactory(AppConstants.TABLE_NAME);
		tableService = new DataTablePersistenceService();

		if (findAllUsers().size() == 0) {
			loadTestData();
		}
	}

	private void loadTestData() {
		User newUser = createUser("testUser", "password");
		DataTable table = tableService.createTestTable();

		newUser.getUsersTables().add(table);
		saveUser(newUser);
	}

	public User createUser(String username, String password) {
		User newUser = new User();
		newUser.setUsername(username);
		saveUser(newUser);
		return newUser;
	}

	public User createUser(User user) throws EntityExistsException {
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(user);
		trans.commit();
		em.close();
		return user;
	}

	public User saveUser(User user) throws EntityExistsException {
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		User savedUser = em.merge(user);
		trans.commit();
		em.close();
		return savedUser;
	}

	@SuppressWarnings("unchecked")
	public Collection<User> findAllUsers() {
		EntityManager em = factory.createEntityManager();
		Query query = em.createQuery("SELECT e FROM User e");
		Collection<User> result = (Collection<User>) query.getResultList();
		em.close();
		return result;
	}

	public User findById(String userEmail) {
		EntityManager em = factory.createEntityManager();
		User result = em.find(User.class, userEmail);
		em.close();
		return result;
	}
}
