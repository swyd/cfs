package com.csf.service.timeslot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistance.dao.timeslot.TimeSlotDao;
import com.csf.persistance.dao.timeslot.TimeSlotUsageDao;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;

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
	public TimeSlotUsage create(User user, Integer timeSlotId) {
		TimeSlotUsage timeSlotUsage = new TimeSlotUsage();
		timeSlotUsage.setUser(user);

		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setId(timeSlotId);
		timeSlotUsage.setTimeSlot(timeSlot);
		
		timeSlotUsage.setUsageDate(new Date());
		
		return timeSlotUsageDao.save(timeSlotUsage);
	}
}
