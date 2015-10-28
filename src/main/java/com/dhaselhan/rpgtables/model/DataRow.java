package com.dhaselhan.rpgtables.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.owlike.genson.annotation.JsonIgnore;

@Entity
@Table(name = "DataRows")
public class DataRow implements Serializable {

	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int id;
	
	private String header;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	private List<String> rowText;

	public List<String> getRowText() {
		return rowText;
	}

	public void setRowText(List<String> rowTexts) {
		this.rowText = rowTexts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
