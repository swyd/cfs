package com.csf.api.rest.transfer.model;

import java.util.Map;

public class UserTransfer {

	private Integer id;

	private String email;

	private String name;

	private String password;

	private Boolean isAdmin;

	private Map<String, Boolean> roles;

	public UserTransfer() {

	}

	public UserTransfer(String email, Integer id, String name, Map<String, Boolean> roles) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.roles = roles;
		
		if(roles.containsKey("ROLE_ADMIN")){
			this.isAdmin = true;
		}else{
			this.isAdmin = false;
		}
			
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Boolean> getRoles() {
		return roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
