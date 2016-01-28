package com.csf.api.rest.transfer.model;

public class TimeSlotTransfer {
	
	private Integer id;
	
	private Integer limit;
	
	private Integer slotsRemaining;
	
	private Boolean isAdvanced;
	
	private Boolean isActive;
	
	private String startsAt;
	
	private Integer coachId;

	private Integer type;
	
	private Integer priority;

	
	public TimeSlotTransfer() {

	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getSlotsRemaining() {
		return slotsRemaining;
	}

	public void setSlotsRemaining(Integer slotsRemaining) {
		this.slotsRemaining = slotsRemaining;
	}

	public String getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(String startsAt) {
		this.startsAt = startsAt;
	}

	public Integer getCoachId() {
		return coachId;
	}

	public void setCoachId(Integer coachId) {
		this.coachId = coachId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(Boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
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
