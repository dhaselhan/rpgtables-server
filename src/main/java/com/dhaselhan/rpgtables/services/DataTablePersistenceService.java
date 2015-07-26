package com.dhaselhan.rpgtables.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dhaselhan.rpgtables.data.DataRow;
import com.dhaselhan.rpgtables.data.DataTable;

public class DataTablePersistenceService {

	private EntityManagerFactory factory;

	public DataTablePersistenceService() {
		factory = Persistence.createEntityManagerFactory(AppConstants.TABLE_NAME);
	}
	
	public DataTable createEmptyTable() {
		DataTable emptyTable = new DataTable();
		emptyTable.setName("Your Table Name");
		List<DataRow> rows = new ArrayList<DataRow>();

		DataRow sampleRow = new DataRow();
		sampleRow.setHeader("Column");
		sampleRow.setRowText(Arrays.asList("Data"));
		rows.add(sampleRow);

		emptyTable.setColumns(rows);
		return emptyTable;
	}

	public DataTable createTestTable() {
		DataTable testTable = new DataTable();
		testTable.setName("Fantasy Gambling Patrons");
		List<DataRow> rows = new ArrayList<DataRow>();

		DataRow testRow = new DataRow();
		testRow.setHeader("Race");
		testRow.setRowText(Arrays.asList("Dwarf", "Human", "Half-Orc", "Gnome",
				"Half-elf"));
		rows.add(testRow);

		DataRow testRow2 = new DataRow();
		testRow2.setHeader("Current State");
		testRow2.setRowText(Arrays.asList("Drunk", "Big Winner", "Big Loser",
				"Desperate To Win", "Just started playing",
				"Criminal, not here to play"));
		rows.add(testRow2);

		DataRow testRow3 = new DataRow();
		testRow3.setHeader("Personality/Actions");
		testRow3.setRowText(Arrays.asList("Happy, Optimistic",
				"Angry, looking to start a fight",
				"Suspicious, answers questions with questions",
				"Friendly, looking for a good time"));
		rows.add(testRow3);

		testTable.setColumns(rows);
		//saveTable(testTable);

		return testTable;
	}

	@SuppressWarnings("unchecked")
	public Collection<DataTable> findAllTables() {
		EntityManager em = factory.createEntityManager();
		Query query = em.createQuery("SELECT e FROM DataTable e");
		Collection<DataTable> result = (Collection<DataTable>) query
				.getResultList();
		em.close();
		return result;
	}

	public DataTable createTable(DataTable newTable) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(newTable);
		trans.commit();
		em.close();
		return newTable;
	}

	public DataTable saveTable(DataTable newTable) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		DataTable savedTable = em.merge(newTable);
		trans.commit();
		em.close();
		return savedTable;
	}

	public DataTable findById(String id) {
		EntityManager em = factory.createEntityManager();
		DataTable result = em.find(DataTable.class, id);
		em.close();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<DataTable> findAllTablesByUsername(String username) {
		EntityManager em = factory.createEntityManager();
		Query query = em
				.createQuery("SELECT e FROM DataTable e where e.owner.username = :username");
		query.setParameter("username", username);
		Collection<DataTable> result = (Collection<DataTable>) query
				.getResultList();
		em.close();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<DataTable> findRecentlyUpdatedTables(int count) {
		EntityManager em = factory.createEntityManager();
		Query query = em
				.createQuery("SELECT e FROM DataTable e ORDER BY e.modifiedDate");
		query.setMaxResults(count);
		Collection<DataTable> result = (Collection<DataTable>) query
				.getResultList();
		em.close();
		return result;
	}

}
