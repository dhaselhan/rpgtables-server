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
			testTable.setName("TestName");
			List<DataRow> rows = new ArrayList<DataRow>();
			DataRow testRow = new DataRow();
			testRow.setRowText(Arrays.asList("Dave", "Steve", "Bob"));
			DataRow testRow2 = new DataRow();
			testRow2.setRowText(Arrays.asList("Johnson", "Smith"));
			rows.add(testRow);
			rows.add(testRow2);
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
