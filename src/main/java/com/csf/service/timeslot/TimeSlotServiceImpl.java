package com.csf.service.timeslot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistance.dao.timeslot.TimeSlotDao;
import com.csf.persistance.dao.timeslot.TimeSlotUsageDao;
import com.csf.persistance.dao.user.UserDao;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	@Autowired
	private TimeSlotDao timeSlotDao;

	@Autowired
	private UserDao userDao;
	
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
	public void deleteUsage(Integer id) {
		timeSlotUsageDao.delete(id);
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

	@Override
	public List<TimeSlotTransfer> findAllWithRemainingForDate(Date date) {
		List<TimeSlot> timeSlots = timeSlotDao.findAll();
		Map<String, Integer> remainingMap = new HashMap<String, Integer>();

		for (TimeSlot timeSlot : timeSlots) {
			remainingMap.put(timeSlot.getStartsAt(), timeSlot.getLimit());
		}

		List<TimeSlotUsage> timeSlotUsages = timeSlotUsageDao.findAllForDate(date);

		for (TimeSlotUsage usage : timeSlotUsages) {
			String key = usage.getTimeSlot().getStartsAt();
			if (remainingMap.containsKey(key)) {
				remainingMap.put(key, remainingMap.get(key) - 1);
			}
		}

		return convertToTimeSlotTransferWithRemaining(timeSlots, remainingMap);
	}

	private List<TimeSlotTransfer> convertToTimeSlotTransferWithRemaining(List<TimeSlot> timeSlots,
			Map<String, Integer> remainingMap) {
		List<TimeSlotTransfer> timeSlotTransfers = new ArrayList<TimeSlotTransfer>();

		for (TimeSlot timeSlot : timeSlots) {
			TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
			timeSlotTransfer.setSlotsRemaining(remainingMap.get(timeSlot.getStartsAt()));
			timeSlotTransfers.add(timeSlotTransfer);
		}

		return timeSlotTransfers;

	}

	@Override
	public TimeSlotUsage create(User user, Integer timeSlotId, Boolean isTommorow) {
		DateTime date = (isTommorow) ? new DateTime().plusDays(1) : new DateTime();
		
		//Todo add training remaining logic
		if(user.getSessionsLeft() == 0){
			throw new RestException("You dont have anymore sessions left");
		}
		
		if (timeSlotUsageDao.checkIfExistsUsageForDate(user.getId(), date.toDate())) {
			throw new RestException("You already have a session sheduled for today");
		}
		
		user.setSessionsLeft(user.getSessionsLeft()-1);
		user = userDao.save(user);
		
		TimeSlotUsage timeSlotUsage = new TimeSlotUsage();
		timeSlotUsage.setUser(user);
		
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setId(timeSlotId);
		timeSlotUsage.setTimeSlot(timeSlot);
		timeSlotUsage.setUsageDate(date.toDate());
		
		
		
		return timeSlotUsageDao.save(timeSlotUsage);
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageForDate(Date date) {
		return timeSlotUsageDao.findAllForDate(date);
	}

	@Override
	public TimeSlotUsage findTimeSlotUsage(Integer timeSlotId) {
		return timeSlotUsageDao.find(timeSlotId);
	}
}
