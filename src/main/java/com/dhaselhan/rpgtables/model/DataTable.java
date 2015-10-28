package com.dhaselhan.rpgtables.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "DataTables")
public class DataTable implements Serializable {

	private static final long serialVersionUID = 2L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@ManyToOne
	private User owner;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderBy("id asc")
	private Collection<DataRow> columns;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	@Transient
	private boolean canEdit = false;

	@PrePersist
	private void updateTimestamp() {
		modifiedDate = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

}
