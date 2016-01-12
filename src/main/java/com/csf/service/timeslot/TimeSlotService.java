package com.csf.service.timeslot;

import java.util.Date;
import java.util.List;

import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;

public interface TimeSlotService {

	List<TimeSlot> findAll();

	TimeSlot find(Integer id);

	TimeSlot save(TimeSlot timeslot);

	void delete(Integer id);

	List<TimeSlotUsage> findAllTimeSlotUsageForToday();

	List<TimeSlotUsage> findAllTimeSlotUsageForTommorow();

	List<TimeSlotUsage> findAllTimeSlotUsage(Date fromDate, Date toDate);

	List<TimeSlotTransfer> findAllWithRemainingForDate(Date date);

	TimeSlotUsage create(User user, Integer timeSlotId);
	
}
