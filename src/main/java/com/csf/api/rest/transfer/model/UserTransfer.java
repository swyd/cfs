package com.csf.api.rest.transfer.model;

import java.util.Date;
import java.util.Map;

public class UserTransfer {

	private Integer id;

	private String email;

	private String name;

	private String surname;

	private String username;

	private String password;

	private Integer sessionsLeft;

	private Integer coachId;

	private Boolean isActive;

	private Boolean isAdmin;

	private Boolean isCoach;

	private Boolean isAdvanced;

	private Date datePaid;

	private Date dateExpiring;

	private Map<String, Boolean> roles;

	public UserTransfer() {

	}

	public UserTransfer(Integer id, String email, String name, String surname, String username, Integer sessionsLeft,
			Boolean isActive, Boolean isAdvanced, Date datePaid, Date dateExpiring, Integer coachId,
			Map<String, Boolean> roles) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.sessionsLeft = sessionsLeft;
		this.isActive = isActive;
		this.isAdvanced = isAdvanced;
		this.dateExpiring = dateExpiring;
		this.datePaid = datePaid;
		this.roles = roles;
		this.coachId = coachId;

		if (roles.containsKey("ROLE_ADMIN")) {
			this.isAdmin = true;
		} else {
			this.isAdmin = false;
		}

		if (roles.containsKey("ROLE_COACH")) {
			this.setIsCoach(true);
		} else {
			this.setIsCoach(false);
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSessionsLeft() {
		return sessionsLeft;
	}

	public void setSessionsLeft(Integer sessionsLeft) {
		this.sessionsLeft = sessionsLeft;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Date getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}

	public Date getDateExpiring() {
		return dateExpiring;
	}

	public void setDateExpiring(Date dateExpiring) {
		this.dateExpiring = dateExpiring;
	}

	public Map<String, Boolean> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(Boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public Boolean getIsCoach() {
		return isCoach;
	}

	public void setIsCoach(Boolean isCoach) {
		this.isCoach = isCoach;
	}

	public Integer getCoachId() {
		return coachId;
	}

	public void setCoachId(Integer coachId) {
		this.coachId = coachId;
	}

}
