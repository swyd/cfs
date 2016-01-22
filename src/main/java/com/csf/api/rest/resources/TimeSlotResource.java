package com.csf.api.rest.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.model.StringTransfer;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;
import com.csf.service.timeslot.TimeSlotService;
import com.csf.service.user.UserService;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotResource {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TimeSlotService timeSlotService;

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/all/usage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeUsages(
			@RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {

		List<TimeSlotUsage> timeSlots = timeSlotService.findAllTimeSlotUsageFromTo(fromDate, toDate);

		return timeSlots;
	}

	@RequestMapping(path = "/all/remaining", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotTransfer> getAllTimeSlotsWithRemaining(
			@RequestParam(name = "forDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date forDate) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (forDate == null) {
			forDate = new Date();
		}

		if (new DateTime(forDate).getDayOfWeek() == 7) {
			return new ArrayList<TimeSlotTransfer>();
		}

		List<TimeSlotTransfer> timeSlots = timeSlotService.findAllWithRemainingForDate(user, forDate);

		return timeSlots;
	}

	@RequestMapping(path = "/usage/{timeSlotId}/{forDate}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public TimeSlotUsage createSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId,
			@PathVariable("forDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date forDate) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (forDate == null) {
			forDate = new Date();
		}

		TimeSlotUsage timeSlotUsage = timeSlotService.create(user, timeSlotId, forDate);

		return timeSlotUsage;
	}

	@RequestMapping(path = "/usage/{timeSlotId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public StringTransfer cancelSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		TimeSlotUsage usage = timeSlotService.findTimeSlotUsage(timeSlotId);

		if (!user.isAdmin() && usage.getUser().getId() != user.getId()) {
			throw new RestException("Ne mozete otkazati trening ako ga niste vi zakazali.");
		}
		if (!user.isAdmin() && isTodayOrLater(usage.getUsageDate())) {
			throw new RestException("Ne mozete otkazati trening posle 15h.");
		}

		timeSlotService.deleteUsage(timeSlotId);
		if (usage.getUser().getId() != user.getId()) {
			user.setSessionsLeft(usage.getUser().getSessionsLeft() + 1);
			userService.save(usage.getUser(), false);
		} else {
			user.setSessionsLeft(usage.getUser().getSessionsLeft() + 1);
			userService.save(user, false);
		}

		return new StringTransfer("Trening otkazan.");
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

	@RequestMapping(path = "/usage/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForDate(
			@RequestParam(name = "forDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date forDate) {

		if (forDate == null) {
			forDate = new Date();
		}
		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsageForDate(forDate);

		return timeSlotUsage;
	}
}
