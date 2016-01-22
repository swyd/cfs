package com.csf.persistence.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "csf_excercise")
public class Excercise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2732785105816112361L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	private String details;
	
	@OneToMany(mappedBy = "excercise")
	private Set<ExcerciseResult> excerciseResults;

	public Excercise() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Set<ExcerciseResult> getExcerciseResults() {
		return excerciseResults;
	}

	public void setExcerciseResults(Set<ExcerciseResult> excerciseResults) {
		this.excerciseResults = excerciseResults;
	}
}
