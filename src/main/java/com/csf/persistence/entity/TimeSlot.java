package com.csf.persistence.entity;

import java.io.Serializable;

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
@Table(name = "csf_time_slot")
public class TimeSlot implements Serializable {

	private static final long serialVersionUID = 6848596770479221825L;

	public enum TIME_SLOT_TYPE {
		WEEKDAY(1, "Radni dan"), HOLIDAY(2, "Praznik"), SATURDAY(3, "Subota"), SUNDAY(4, "Nedelja");

		private int key;
		private String description;

		TIME_SLOT_TYPE(int key, String description) {
			this.key = key;
			this.description = description;
		}

		public int getKey() {
			return this.key;
		}

		public String getDescription() {
			return this.description;
		}

	}

	public TimeSlot() {
		/* Reflection instantiation */
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "starts_at")
	private String startsAt;

	private Integer limit;

	@Column(name = "isadvanced")
	private Boolean isAdvanced;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "coach_id")
	private User coach;

	@Column(name = "type")
	private Integer type;

	@Column(name = "isactive")
	private Boolean isActive;

	private Integer priority;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(String startsAt) {
		this.startsAt = startsAt;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Boolean getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(Boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
