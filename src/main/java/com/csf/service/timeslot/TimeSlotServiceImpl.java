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
	public List<TimeSlotTransfer> findAllWithRemainingForDate(User user, Date date) {
		List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		Map<String, Integer> remainingMap = new HashMap<String, Integer>();
		Boolean isAdvanced = user.getIsAdvanced();
		for (TimeSlot timeSlot : timeSlotDao.findAll()) {
			if (isAdvanced && timeSlot.getIsAdvanced()) {
				timeSlots.add(timeSlot);
			} else if (!isAdvanced && !timeSlot.getIsAdvanced()) {
				timeSlots.add(timeSlot);
			}
		}

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

		return convertToTimeSlotTransferWithRemaining(timeSlots, remainingMap, isAdvanced);
	}

	private List<TimeSlotTransfer> convertToTimeSlotTransferWithRemaining(List<TimeSlot> timeSlots,
			Map<String, Integer> remainingMap, Boolean isAdvanced) {
		List<TimeSlotTransfer> timeSlotTransfers = new ArrayList<TimeSlotTransfer>();

		if (isAdvanced) {
			for (TimeSlot timeSlot : timeSlots) {
				TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
				timeSlotTransfer.setSlotsRemaining(remainingMap.get(timeSlot.getStartsAt()));
				timeSlotTransfers.add(timeSlotTransfer);
			}
		} else {
			Boolean openFive = checkIfAllAreBooked(remainingMap);
			if (openFive) {
				for (TimeSlot timeSlot : timeSlots) {
					TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
					timeSlotTransfer.setSlotsRemaining(remainingMap.get(timeSlot.getStartsAt()));
					timeSlotTransfers.add(timeSlotTransfer);
				}
			} else {
				for (TimeSlot timeSlot : timeSlots) {
					if (!timeSlot.getStartsAt().equals("17")) {
						TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
						timeSlotTransfer.setSlotsRemaining(remainingMap.get(timeSlot.getStartsAt()));
						timeSlotTransfers.add(timeSlotTransfer);
					}
				}
			}

		}

		return timeSlotTransfers;

	}

	private Boolean checkIfAllAreBooked(Map<String, Integer> map) {
		for (String key : map.keySet()) {
			if (!key.equals("17")) {
				if (map.get(key) > 0) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public TimeSlotUsage create(User user, Integer timeSlotId, Date forDate) {
		if (user.getSessionsLeft() == 0) {
			throw new RestException("Nemate vise treninga, uplatite clanarinu.");
		}

		if (timeSlotUsageDao.checkIfExistsUsageForDate(user.getId(), forDate)) {
			throw new RestException("Vec imate zakazan trening za danas");
		}

		Long sessionsUsed = timeSlotUsageDao.getSessionsRemainingForDateAndSlot(timeSlotId, forDate);

		if (sessionsUsed == 14) {
			throw new RestException("Nema vise slobodnih mesta za zeljeni termin..");
		}

		if (checkIsTodayAfterThree(new DateTime(forDate))) {
			throw new RestException("Zakazivanje treninga nije moguce posle 15h tekuceg dana, ukoliko ima slobodnih mesta pozovite.");
		}

		user.setSessionsLeft(user.getSessionsLeft() - 1);
		user = userDao.save(user);

		TimeSlotUsage timeSlotUsage = new TimeSlotUsage();
		timeSlotUsage.setUser(user);

		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setId(timeSlotId);
		timeSlotUsage.setTimeSlot(timeSlot);
		timeSlotUsage.setUsageDate(forDate);

		return timeSlotUsageDao.save(timeSlotUsage);
	}

	private Boolean checkIsTodayAfterThree(DateTime date) {
		DateTime now = new DateTime();
		if (now.getDayOfYear() == date.getDayOfYear()) {
			if (date.getHourOfDay() >= 15) {
				return true;
			}
		}else if(now.getDayOfYear() > date.getDayOfYear()){
			return true;
		}
		return false;
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageForDate(Date date) {
		return timeSlotUsageDao.findAllForDate(date);
	}

	@Override
	public TimeSlotUsage findTimeSlotUsage(Integer timeSlotId) {
		return timeSlotUsageDao.find(timeSlotId);
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageFromTo(Date fromDate, Date toDate) {
		return timeSlotUsageDao.findAllTimeSlotUsageFromTo(fromDate, toDate);
	}
}
