package com.csf.persistence.entity;

import java.io.Serializable;

//@Entity(name = "csf_user_role")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5345851954854298797L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String role;

	public UserRole() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
