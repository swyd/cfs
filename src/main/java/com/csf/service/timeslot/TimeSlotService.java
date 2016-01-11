package com.csf.service.timeslot;

import java.util.Date;
import java.util.List;

import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;

public interface TimeSlotService {

	List<TimeSlot> findAll();

	TimeSlot find(Integer id);

	TimeSlot save(TimeSlot timeslot);

	void delete(Integer id);

	List<TimeSlotUsage> findAllTimeSlotUsageForToday();

	List<TimeSlotUsage> findAllTimeSlotUsageForTommorow();

	List<TimeSlotUsage> findAllTimeSlotUsage(Date fromDate, Date toDate);
	
}
