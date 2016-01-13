package com.csf.api.rest.resources;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.StringTransfer;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistence.entity.TimeSlot;
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

	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotTransfer> getAllTimeSlots() {

		List<TimeSlot> timeSlots = timeSlotService.findAll();

		return TransferConverterUtil.convertTimeSlotToTransfer(timeSlots);
	}

	@RequestMapping(path = "/all/remaining", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotTransfer> getAllTimeSlotsWithRemaining(
			@RequestParam(name = "isTommorow", required = false, defaultValue = "false") Boolean isTommorow) {

		DateTime date = new DateTime();
		if (isTommorow) {
			date = date.plusDays(1);
		}

		List<TimeSlotTransfer> timeSlots = timeSlotService.findAllWithRemainingForDate(date.toDate());

		return timeSlots;
	}

	@RequestMapping(path = "/usage/{timeSlotId}/{isTommorow}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public TimeSlotUsage createSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId, @PathVariable("isTommorow") Boolean isTommorow) {
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		TimeSlotUsage timeSlotUsage = timeSlotService.create(user, timeSlotId, isTommorow);

		return timeSlotUsage;
	}
	
	@RequestMapping(path = "/usage/{timeSlotId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public StringTransfer cancelSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId) {
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		TimeSlotUsage usage = timeSlotService.findTimeSlotUsage(timeSlotId);
		if(usage.getUser().getId() != user.getId()){
			throw new RestException("You cannot cancel a training that is not shedueled by you.");
		}

		timeSlotService.deleteUsage(timeSlotId);
		user.setSessionsLeft(user.getSessionsLeft()+1);
		userService.save(user, false);

		return new StringTransfer("Training session canceled.");
	}
	
	@RequestMapping(path = "/usage/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForTommorow(
			@RequestParam(name = "isTommorow", required = false, defaultValue = "false") Boolean isTommorow) {

		DateTime date = new DateTime();
		if (isTommorow) {
			date = date.plusDays(1);
		}
		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsageForDate(date.toDate());

		return timeSlotUsage;
	}

	// @RequestMapping(path = "/usage/all/today", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON)
	// @PreAuthorize(value = "isAuthenticated()")
	// public List<TimeSlotUsage> getAllTimeSlotUsagesForToday() {
	//
	// List<TimeSlotUsage> timeSlotUsage =
	// timeSlotService.findAllTimeSlotUsageForToday();
	//
	// return timeSlotUsage;
	// }
	//
	// @RequestParam(name = "fromDate", required = false)
	// @DateTimeFormat(pattern = "ddMMyyyy") Date fromDate,
	// @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern
	// = "ddMMyyyy") Date toDate
	//
	// @RequestMapping(path = "/usage/all/tommorow", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON)
	// @PreAuthorize(value = "isAuthenticated()")
	// public List<TimeSlotUsage> getAllTimeSlotUsagesForTommorow() {
	//
	// List<TimeSlotUsage> timeSlotUsage =
	// timeSlotService.findAllTimeSlotUsageForTommorow();
	//
	// return timeSlotUsage;
	// }
}
