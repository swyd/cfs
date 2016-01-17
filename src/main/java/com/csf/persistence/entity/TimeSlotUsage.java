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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQueries({
		@NamedQuery(name = "TimeSlotUsage.findAllForDate", query = "SELECT t FROM TimeSlotUsage t WHERE "
				+ "t.usageDate = :usageDate"),
		@NamedQuery(name = "TimeSlotUsage.getSessionsRemainingForDateAndSlot", query = "SELECT COUNT(t) FROM TimeSlotUsage t WHERE "
				+ "t.usageDate = :forDate AND t.timeSlot.id = :timeSlotId"),
		@NamedQuery(name = "TimeSlotUsage.findAllUsageFromTo", query = "SELECT t FROM TimeSlotUsage t WHERE "
				+ "t.usageDate >= :fromDate AND t.usageDate <= :toDate"),
		@NamedQuery(name = "TimeSlotUsage.removeAllSessionsForUser", query = "DELETE FROM TimeSlotUsage t WHERE "
				+ "t.user.id = :id"),
		@NamedQuery(name = "TimeSlotUsage.checkIfExistsForDate", query = "SELECT COUNT(t) FROM TimeSlotUsage t WHERE "
				+ "t.usageDate = :usageDate AND t.user.id = :userId") })

@Table(name = "csf_time_slot_usage")
public class TimeSlotUsage implements Serializable {

	private static final long serialVersionUID = 3823598790976035612L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "time_slot_id")
	private TimeSlot timeSlot;

	@Column(name = "usage_date")
	private Date usageDate;

	public TimeSlotUsage() {
		/* Reflection instantiation */
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Date getUsageDate() {
		return usageDate;
	}

	public void setUsageDate(Date usageDate) {
		this.usageDate = usageDate;
	}
}
