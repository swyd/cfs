package com.csf.api.rest.resources;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

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

import com.csf.api.rest.transfer.model.StringTransfer;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;
import com.csf.service.timeslot.TimeSlotService;
import com.csf.service.user.UserService;

@RestController
@RequestMapping("/timeslot/usage")
public class TimeSlotUsageResource {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TimeSlotService timeSlotService;

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeUsages(
			@RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {

		List<TimeSlotUsage> timeSlots = timeSlotService.findAllTimeSlotUsageFromTo(fromDate, toDate);

		return timeSlots;
	}

	@RequestMapping(path = "/all/forDate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForDate(
			@RequestParam(name = "forDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date forDate) {

		if (forDate == null) {
			forDate = new Date();
		}
		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsageForDate(forDate);

		return timeSlotUsage;
	}

	@RequestMapping(path = "/{timeSlotId}/{forDate}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
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

	@RequestMapping(path = "/{timeSlotId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public StringTransfer cancelSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId) {
		timeSlotService.deleteUsage(timeSlotId);

		return new StringTransfer("Trening otkazan.");
	}

}
