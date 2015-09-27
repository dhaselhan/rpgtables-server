package com.dhaselhan.rpgtables.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="owner")
	@OrderBy("id asc")
	private Collection<DataTable> usersTables;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Collection<DataTable> getUsersTables() {
		return usersTables;
	}

}
