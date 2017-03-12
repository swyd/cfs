package com.csf.service.timeslot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

		if (!user.isAdmin() && !usage.getUser().getId().equals(user.getId())) {
			throw new RestException("Ne mozete otkazati trening ako ga niste vi zakazali.");
		}
		if (!user.isAdmin()) {
			if (!usage.getTimeSlot().getIsInTheMorning() && isTodayLaterThen(usage.getUsageDate(), 12)) {
				throw new RestException("Ne mozete otkazati trening posle 12h.");
			} else if (usage.getTimeSlot().getIsInTheMorning() && isTodayLaterThen(usage.getUsageDate(), 0)) {
				throw new RestException("Ne mozete otkazati trening posle 00h.");
			}
		}

		timeSlotUsageDao.delete(id);

		if (!usage.getUser().getId().equals(user.getId())) {
			user.setSessionsLeft(usage.getUser().getSessionsLeft() + 1);
			userDao.save(usage.getUser());
		} else {
			user.setSessionsLeft(user.getSessionsLeft() + 1);
			userDao.save(user);
		}
	}

	private Boolean isTodayLaterThen(Date date, Integer time) {
		DateTime now = new DateTime();
		DateTime input = new DateTime(date);
		if (now.getDayOfYear() == input.getDayOfYear()) {
			if (now.getHourOfDay() >= time) {
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
		List<TimeSlot> timeSlotsMorning = timeSlotDao
				.findAllActiveForType(TIME_SLOT_TYPE.findType(new DateTime(date).getDayOfWeek(), true));
		List<TimeSlot> timeSlotsAfternoon = timeSlotDao
				.findAllActiveForType(TIME_SLOT_TYPE.findType(new DateTime(date).getDayOfWeek(), false));

		Map<String, Integer> remainingMap = new HashMap<String, Integer>();

		for (TimeSlot timeSlot : timeSlotsMorning) {
			remainingMap.put(timeSlot.getStartsAt(), timeSlot.getLimit());
		}
		for (TimeSlot timeSlot : timeSlotsAfternoon) {
			remainingMap.put(timeSlot.getStartsAt(), timeSlot.getLimit());
		}

		List<TimeSlotUsage> timeSlotUsages = timeSlotUsageDao.findAllForDate(date);

		for (TimeSlotUsage usage : timeSlotUsages) {
			String key = usage.getTimeSlot().getStartsAt();
			if (remainingMap.containsKey(key)) {
				remainingMap.put(key, remainingMap.get(key) - 1);
			}
		}

		List<TimeSlotTransfer> retVal = convertToTimeSlotTransferWithRemaining(timeSlotsMorning, remainingMap);
		retVal.addAll(convertToTimeSlotTransferWithRemaining(timeSlotsAfternoon, remainingMap));
		return retVal;
	}

	private List<TimeSlotTransfer> convertToTimeSlotTransferWithRemaining(List<TimeSlot> timeSlots,
			Map<String, Integer> remainingMap) {
		List<TimeSlotTransfer> timeSlotTransfers = new ArrayList<TimeSlotTransfer>();
		Set<Integer> prios = new HashSet<Integer>();
		boolean openNextPrio = true;
		for (int i = 0; i < timeSlots.size(); i++) {
			TimeSlot timeSlot = timeSlots.get(i);
			TimeSlotTransfer timeSlotTransfer = TransferConverterUtil.convertTimeSlotToTransfer(timeSlot);
			Integer slotsRemaining = remainingMap.get(timeSlot.getStartsAt());
			if (i > 0) {
				if (timeSlot.getPriority() == timeSlots.get(i - 1).getPriority()
						&& prios.contains(timeSlot.getPriority())) {
					if (slotsRemaining > 0) {
						openNextPrio = false;
					}
					prios.add(timeSlot.getPriority());
					timeSlotTransfer.setSlotsRemaining(slotsRemaining);
					timeSlotTransfers.add(timeSlotTransfer);
				} else {
					if (remainingMap.get(timeSlot.getStartsAt()) < timeSlot.getLimit() || openNextPrio) {
						if (slotsRemaining > 0) {
							openNextPrio = false;
						}
						prios.add(timeSlot.getPriority());
						timeSlotTransfer.setSlotsRemaining(slotsRemaining);
						timeSlotTransfers.add(timeSlotTransfer);
					}
				}
			} else {
				if (slotsRemaining > 0) {
					openNextPrio = false;
				}
				prios.add(timeSlot.getPriority());
				timeSlotTransfer.setSlotsRemaining(slotsRemaining);
				timeSlotTransfers.add(timeSlotTransfer);
			}
		}
		return timeSlotTransfers;
	}

	@Override
	public TimeSlotUsage create(User user, Integer timeSlotId, Date forDate) {
		if (user.getSessionsLeft() == 0) {
			throw new RestException("Nemate vise treninga, uplatite clanarinu.");
		}

		if (!user.isAdmin() && checkIsMembershipExpired(new DateTime(user.getDateExpiring()))) {
			throw new RestException("Clanarina vam je istekla. Molimo vas kontaktirajte vaseg trenera.");
		}

		if (!user.isAdmin() && checkIfAfterTommorow(new DateTime(forDate))) {
			throw new RestException("Treninzi se ne mogu zakazivati pre 12h po Srpskom lokalnom vremenu.");
		}

		if (!user.isAdmin() && timeSlotUsageDao.checkIfExistsUsageForDate(user.getId(), forDate)) {
			throw new RestException("Vec imate zakazan trening za danas");
		}

		Long sessionsUsed = timeSlotUsageDao.getSessionsRemainingForDateAndSlot(timeSlotId, forDate);
		TimeSlot existingSlot = timeSlotDao.find(timeSlotId);
		
//		if (sessionsUsed == existingSlot.getLimit().longValue()) {
//			throw new RestException("Nema vise slobodnih mesta za zeljeni termin..");
//		}

		if (checkIsAfterSlotStartDate(existingSlot.getStartsAt().toString(), new DateTime(forDate))) {
			throw new RestException("Zakazivanje treninga nije moguce, nakon sto je trening poceo.");
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

	private Boolean checkIfAfterTommorow(DateTime date) {
		DateTime now = new DateTime();
		if ((date.getDayOfYear() - now.getDayOfYear()) > 1) {
			return true;
		}
		return false;
	}

	private Boolean checkIsMembershipExpired(DateTime expirationDate) {
		DateTime now = new DateTime();
		if (now.isAfter(expirationDate)) {
			return true;
		}
		return false;
	}

	private Boolean checkIsAfterSlotStartDate(String slotStartTime, DateTime date) {
		DateTime now = new DateTime();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
		DateTime startTime = dtf.parseDateTime(slotStartTime);
		if (now.getDayOfYear() == date.getDayOfYear()) {
			if (now.getMinuteOfDay() >= startTime.getMinuteOfDay()) {
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
