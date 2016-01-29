package com.csf.service.timeslot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistance.dao.timeslot.TimeSlotDao;
import com.csf.persistance.dao.timeslot.TimeSlotUsageDao;
import com.csf.persistance.dao.user.UserDao;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlot.TIME_SLOT_TYPE;
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
		// check if timeslot like this already exists
		if (timeSlot.getId() == null && timeSlotDao.checkIfExists(timeSlot.getType(), timeSlot.getStartsAt())) {
			throw new RestException(
					"Vec postoji Trening u terminu: " + timeSlot.getStartsAt() + "h, tipa: " + timeSlot.getType());
		}
		return timeSlotDao.save(timeSlot);
	}

	@Override
	public void delete(Integer id) {
		timeSlotDao.delete(id);
	}

	@Override
	public void deleteUsage(Integer id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		TimeSlotUsage usage = findTimeSlotUsage(id);

		if (!user.isAdmin() && usage.getUser().getId() != user.getId()) {
			throw new RestException("Ne mozete otkazati trening ako ga niste vi zakazali.");
		}
		if (!user.isAdmin() && isTodayOrLater(usage.getUsageDate())) {
			throw new RestException("Ne mozete otkazati trening posle 15h.");
		}

		timeSlotUsageDao.delete(id);

		if (usage.getUser().getId() != user.getId()) {
			user.setSessionsLeft(usage.getUser().getSessionsLeft() + 1);
			userDao.save(usage.getUser());
		} else {
			user.setSessionsLeft(usage.getUser().getSessionsLeft() + 1);
			userDao.save(user);
		}
	}

	private Boolean isTodayOrLater(Date date) {
		DateTime now = new DateTime();
		DateTime input = new DateTime(date);
		if (now.getDayOfYear() == input.getDayOfYear()) {
			if (now.getHourOfDay() >= 15) {
				return true;
			}
		} else if (now.getDayOfYear() > input.getDayOfYear()) {
			return true;
		}
		return false;
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
		//TODO implement check for trainer
		for (TimeSlot timeSlot : timeSlotDao
				.findAllActiveForType(TIME_SLOT_TYPE.findType(new DateTime(date).getDayOfWeek()))) {
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
			// Make algorithm and take into account priority of opening slots
			boolean openNextPrio = true;
			for (int i = 0; i < timeSlots.size(); i++) {
				TimeSlot timeSlot = timeSlots.get(i);

				if (i > 0) {
					// check second
					if (timeSlot.getPriority() == timeSlots.get(i - 1).getPriority()) {
						TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
						Integer slotsRemaining = remainingMap.get(timeSlot.getStartsAt());
						if (slotsRemaining > 0) {
							openNextPrio = false;
						}
						timeSlotTransfer.setSlotsRemaining(slotsRemaining);
						timeSlotTransfers.add(timeSlotTransfer);
					} else {
						if (openNextPrio) {
							TimeSlotTransfer timeSlotTransfer = TransferConverterUtil
									.convertTimeSlotToTransfer(timeSlot);
							Integer slotsRemaining = remainingMap.get(timeSlot.getStartsAt());
							if (slotsRemaining > 0) {
								openNextPrio = false;
							}
							timeSlotTransfer.setSlotsRemaining(slotsRemaining);
							timeSlotTransfers.add(timeSlotTransfer);
						}
					}
				} else {
					// Add first
					TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
					Integer slotsRemaining = remainingMap.get(timeSlot.getStartsAt());
					if (slotsRemaining > 0) {
						openNextPrio = false;
					}
					timeSlotTransfer.setSlotsRemaining(slotsRemaining);
					timeSlotTransfers.add(timeSlotTransfer);
				}

			}
		}

		return timeSlotTransfers;

	}

	// private Boolean checkIfAllBeforeAreFull(Integer iterator, TimeSlot
	// timeSlots, Map<String, Integer> map) {
	// TimeSlotTransfer timeSlotTransfer =
	// TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
	// Integer slotsRemaining = remainingMap.get(timeSlot.getStartsAt());
	// if (slotsRemaining > 0) {
	// openNextPrio = false;
	// }
	// timeSlotTransfer.setSlotsRemaining(slotsRemaining);
	// timeSlotTransfers.add(timeSlotTransfer);
	// }

	@Override
	public TimeSlotUsage create(User user, Integer timeSlotId, Date forDate) {
		if (user.getSessionsLeft() == 0) {
			throw new RestException("Nemate vise treninga, uplatite clanarinu.");
		}

		if (!user.isAdmin() && timeSlotUsageDao.checkIfExistsUsageForDate(user.getId(), forDate)) {
			throw new RestException("Vec imate zakazan trening za danas");
		}

		Long sessionsUsed = timeSlotUsageDao.getSessionsRemainingForDateAndSlot(timeSlotId, forDate);

		if (sessionsUsed == 14) {
			throw new RestException("Nema vise slobodnih mesta za zeljeni termin..");
		}

		if (!user.isAdmin() && checkIsTodayAfterThree(new DateTime(forDate))) {
			throw new RestException(
					"Zakazivanje treninga nije moguce posle 15h tekuceg dana, ukoliko ima slobodnih mesta pozovite.");
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
			if (now.getHourOfDay() >= 15) {
				return true;
			}
		} else if (now.getDayOfYear() > date.getDayOfYear()) {
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
