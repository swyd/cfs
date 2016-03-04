package com.csf.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@Table(name = "csf_membership")
public class Membership implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Membership() {

	}

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Integer price;
	
	@Column(name="number_of_sessions")
	private Integer numberOfSessions;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getNumberOfSessions() {
		return numberOfSessions;
	}

	public void setNumberOfSessions(Integer numberOfSessions) {
		this.numberOfSessions = numberOfSessions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
