package com.csf.api.rest.transfer.model;

public class TimeSlotTransfer {
	
	private Integer id;
	
	private Integer limit;
	
	private Integer slotsRemaining;
	
	private String startsAt;
	
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
}
