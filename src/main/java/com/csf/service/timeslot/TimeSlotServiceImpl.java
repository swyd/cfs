package com.csf.service.timeslot;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.persistance.dao.timeslot.TimeSlotDao;
import com.csf.persistance.dao.timeslot.TimeSlotUsageDao;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	@Autowired
	private TimeSlotDao timeSlotDao;
	
	@Autowired
	private TimeSlotUsageDao timeSlotUsageDao;

	@Override
	public List<TimeSlot> findAll() {
		return timeSlotDao.findAll();
	}

	@Override
	public TimeSlot find(Integer id) {
		return timeSlotDao.find(id);
	}

	@Override
	public TimeSlot save(TimeSlot timeSlot) {
		return timeSlotDao.save(timeSlot);
	}

	@Override
	public void delete(Integer id) {
		timeSlotDao.delete(id);
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageForToday() {
		return timeSlotUsageDao.findAll();
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageForTommorow() {
		return timeSlotUsageDao.findAll();
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsage(Date fromDate, Date toDate) {
		return timeSlotUsageDao.findAll(fromDate, toDate);
	}

}
