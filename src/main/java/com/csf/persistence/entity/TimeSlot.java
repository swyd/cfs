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
		WEEKDAY_ODD_AFTERNOON(1, "Radni dan neparni popodne", false), WEEKDAY_EVEN_AFTERNOON(4,
				"Radni dan parni popodne", false), SATURDAY_AFTERNOON(2, "Subota popodne", false), SUNDAY(3, "Nedelja",
						false), WEEKDAY_ODD_MORNING(5, "Radni dan neparni prepodne", true), WEEKDAY_EVEN_MORNING(6,
								"Radni dan parni prepodne", true), SATURDAY_MORNING(7, "Subota prepodne", true);

		private int key;
		private String description;
		private Boolean isInTheMorning;

		TIME_SLOT_TYPE(int key, String description, Boolean isInTheMorning) {
			this.key = key;
			this.description = description;
			this.isInTheMorning = isInTheMorning;
		}

		public int getKey() {
			return this.key;
		}

		public String getDescription() {
			return this.description;
		}

		public Boolean getIsInTheMorning() {
			return this.isInTheMorning;
		}

		public static Integer findType(int dayOfWeek, Boolean isInTheMorning) {
			switch (dayOfWeek) {
			case 1:
			case 3:
			case 5: {
				if (isInTheMorning) {
					return WEEKDAY_ODD_MORNING.getKey();
				} else {
					return WEEKDAY_ODD_AFTERNOON.getKey();
				}
			}
			case 2:
			case 4: {
				if (isInTheMorning) {
					return WEEKDAY_EVEN_MORNING.getKey();
				} else {
					return WEEKDAY_EVEN_AFTERNOON.getKey();
				}
			}
			case 6:
				if (isInTheMorning) {
					return SATURDAY_MORNING.getKey();
				} else {
					return SATURDAY_AFTERNOON.getKey();
				}
			default:
				return SUNDAY.getKey();
			}
		}

		public static Boolean getIsInTheMorning(int dayOfWeek) {
			switch (dayOfWeek) {
			case 1: {
				return WEEKDAY_ODD_AFTERNOON.getIsInTheMorning();
			}
			case 2: {
				return SATURDAY_AFTERNOON.getIsInTheMorning();
			}
			case 7: {
				return SATURDAY_MORNING.getIsInTheMorning();
			}
			case 4: {
				return WEEKDAY_EVEN_AFTERNOON.getIsInTheMorning();
			}
			case 5: {
				return WEEKDAY_ODD_MORNING.getIsInTheMorning();
			}
			case 6: {
				return WEEKDAY_EVEN_MORNING.getIsInTheMorning();
			}
			default: {
				return SUNDAY.getIsInTheMorning();
			}
			}
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

	@Column(name = "timeslot_limit")
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

	@Column(name = "priority")
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

	public Boolean getIsInTheMorning() {
		return TIME_SLOT_TYPE.getIsInTheMorning(this.type);
	}

}
