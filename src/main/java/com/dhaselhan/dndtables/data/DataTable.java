package com.dhaselhan.dndtables.data;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DataTables")
public class DataTable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;

	@OneToMany(cascade = CascadeType.PERSIST)
	private Collection<DataRow> columns;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<DataRow> getColumns() {
		return columns;
	}

	public void setColumns(Collection<DataRow> columns) {
		this.columns = columns;
	}

}
