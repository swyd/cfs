package com.csf.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
// @NamedQueries({
// @NamedQuery(name = "Contract.findAllForUser", query = "SELECT c FROM Contract
// c WHERE c.user.id = :id "),
// @NamedQuery(name = "Contract.findAllForUserDates", query = "SELECT c FROM
// Contract c WHERE c.user.id = :id "
// + "AND c.creationDate >= :fromDate AND c.creationDate <= :toDate AND c.name
// LIKE :name AND c.licencePlate "
// + "LIKE :licencePlate AND (:packetId IS NULL OR c.packet.id = :packetId)"),
// @NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c
// WHERE c.creationDate >="
// + " :fromDate AND c.creationDate <= :toDate AND c.name LIKE :name AND
// c.licencePlate "
// + "LIKE :licencePlate AND (:packetId IS NULL OR c.packet.id = :packetId )"),
// @NamedQuery(name = "Contract.searchForContracts", query = "SELECT c FROM
// Contract c WHERE c.creationDate >="
// + " :fromDate AND c.creationDate <= :toDate AND c.name LIKE :name"
// + " AND c.licencePlate LIKE :licencePlate AND (:id IS NULL OR c.id = :id) AND
// c.active = :active"),
// @NamedQuery(name = "Contract.searchForUserContracts", query = "SELECT c FROM
// Contract c WHERE c.creationDate >="
// + " :fromDate AND c.creationDate <= :toDate AND c.name LIKE :name AND
// c.user.id = :userId"
// + " AND c.licencePlate LIKE :licencePlate AND (:id IS NULL OR c.id = :id) AND
// c.active = :active") })// AND c.active = :active
@Table(name = "csf_news")
public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 434717241481550174L;

	public News() {

	}

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "date_created")
	private Date dateCreated;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
