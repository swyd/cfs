package com.csf.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "csf_user")
public class User implements Serializable, UserDetails {

	public enum USER_ROLE {
		ADMIN(1, "ROLE_ADMIN"), USER(2, "ROLE_USER"), COACH(3, "ROLE_COACH");

		private String value;
		private int key;

		USER_ROLE(int key, String value) {
			this.key = key;
			this.value = value;
			;
		}

		public int getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6870560250245412311L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(unique = true, length = 16, nullable = false)
	private String email;

	private String name;

	private String surname;

	@Column(nullable = false)
	private String password;

	@Column(name = "sessions_left")
	private Integer sessionsLeft;

	@Column(name = "role")
	private Integer role;

	@Column(name = "isactive")
	private Boolean isActive;

	@Column(name = "date_paid")
	private Date datePaid;

	@Column(name = "date_expiring")
	private Date dateExpiring;

	@Column(name = "username", unique = true, length = 16, nullable = false)
	private String username;

	@Column(name = "isadvanced")
	private Boolean isAdvanced;
	
	@ManyToOne(cascade={CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name="coach")
	private User coach;
	
	@OneToMany(mappedBy="coach", fetch = FetchType.LAZY)
	private Set<User> trainees;

	public User() {
		/* Reflection instantiation */
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority(USER_ROLE.USER.getValue()));

		if (role.equals(USER_ROLE.ADMIN.getKey())) {
			authorities.add(new SimpleGrantedAuthority(USER_ROLE.ADMIN.getValue()));
		}

		if (role.equals(USER_ROLE.COACH.getKey())) {
			authorities.add(new SimpleGrantedAuthority(USER_ROLE.COACH.getValue()));
		}

		return authorities;
	}

	public boolean isAdmin() {
		if (this.getRole().equals(USER_ROLE.ADMIN.getKey())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isActive;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Boolean getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(Boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}

	public Set<User> getTrainees() {
		return trainees;
	}

	public void setTrainees(Set<User> trainees) {
		this.trainees = trainees;
	}

}
