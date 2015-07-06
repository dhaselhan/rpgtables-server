package com.dhaselhan.rpgtables.data;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "DataTables")
public class DataTable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;
	
	@ManyToOne
	private User owner;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderBy("id asc")
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
