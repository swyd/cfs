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

import org.joda.time.DateTime;

@Entity
@Table(name = "csf_time_slot")
public class TimeSlot implements Serializable {

	private static final long serialVersionUID = 6848596770479221825L;
	//TODO FIX SO THAT GET/SET RETURN ENUMS
	public enum TIME_SLOT_TYPE {
		WEEKDAY(1), HOLIDAY(2), WEEKEND(3);

		private int key;

		TIME_SLOT_TYPE(int key) {
			this.key = key;
			;
		}

		public int getKey() {
			return this.key;
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

	@Column(name = "active_on_days")
	private String daysActive;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id")
	private User coach;

	@Column(name = "type")
	private Integer type;

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

	public String getDaysActive() {
		return daysActive;
	}

	public void setDaysActive(String daysActive) {
		this.daysActive = daysActive;
	}

	public boolean isActiveForDate(DateTime now) {
		String[] days = this.daysActive.split("");
		if (days[now.getDayOfWeek() - 1].equals("1")) {
			return true;
		}
		return false;
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

}
