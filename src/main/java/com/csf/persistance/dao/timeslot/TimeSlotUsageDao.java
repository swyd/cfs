package com.csf.persistance.dao.timeslot;

import java.util.Date;
import java.util.List;

import com.csf.persistance.dao.GenericDao;
import com.csf.persistence.entity.TimeSlotUsage;

public interface TimeSlotUsageDao extends GenericDao<TimeSlotUsage, Integer>{

	List<TimeSlotUsage> findAll(Date fromDate, Date toDate);

	List<TimeSlotUsage> findAllForDate(Date date);

	Boolean checkIfExistsUsageForDate(Integer userId, Date date);

	Long getSessionsRemainingForDateAndSlot(Integer timeSlotId, Date forDate);

	List<TimeSlotUsage> findAllTimeSlotUsageFromTo(Date fromDate, Date toDate);

	void removeAllSessionsForUser(Integer id);
	
}
