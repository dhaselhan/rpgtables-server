package com.dhaselhan.dndtables.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dhaselhan.dndtables.data.DataRow;
import com.dhaselhan.dndtables.data.DataTable;

public class DataTablePersistenceService {

	private EntityManagerFactory factory;

	public DataTablePersistenceService() {
		factory = Persistence.createEntityManagerFactory("dndtables");

		loadTestData();
	}

	private void loadTestData() {
		if (findAllTables().size() == 0) {
			DataTable testTable = new DataTable();
			testTable.setName("Fantasy Gambling Patrons");
			List<DataRow> rows = new ArrayList<DataRow>();
			
			DataRow testRow = new DataRow();
			testRow.setHeader("Race");
			testRow.setRowText(Arrays.asList("Dwarf","Human","Half-Orc","Gnome","Half-elf"));
			rows.add(testRow);
			
			DataRow testRow2 = new DataRow();
			testRow2.setHeader("Current State");
			testRow2.setRowText(Arrays.asList("Drunk", "Big Winner", "Big Loser", "Desperate To Win", "Just started playing", "Criminal, not here to play"));
			rows.add(testRow2);

			DataRow testRow3 = new DataRow();
			testRow3.setHeader("Personality/Actions");
			testRow3.setRowText(Arrays.asList("Happy, Optimistic", "Angry, looking to start a fight", "Suspicious, answeres questions with questions", "Friendly, looking for a good time"));
			rows.add(testRow3);

			testTable.setColumns(rows);
			saveTable(testTable);
		}
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

	public void saveTable(DataTable newTable) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(newTable);
		trans.commit();
		em.close();
	}

	public DataTable findById(String id) {
		EntityManager em = factory.createEntityManager();
		DataTable result = em.find(DataTable.class, id);
		em.close();
		return result;
	}
}
