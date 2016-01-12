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

import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;
import com.csf.service.timeslot.TimeSlotService;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotResource {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TimeSlotService timeSlotService;

	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotTransfer> getAllTimeSlots() {

		List<TimeSlot> timeSlots = timeSlotService.findAll();

		return TransferConverterUtil.convertTimeSlotToTransfer(timeSlots);
	}

	@RequestMapping(path = "/all/remaining", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotTransfer> getAllTimeSlotsWithRemaining() {

		List<TimeSlotTransfer> timeSlots = timeSlotService.findAllWithRemainingForDate(new Date());

		return timeSlots;
	}

	@RequestMapping(path = "/usage/all/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForToday() {
		// TODO define this by date and merge with bellow
		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsageForToday();

		return timeSlotUsage;
	}

	@RequestMapping(path = "/usage/all/tommorow", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForTommorow() {

		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsageForTommorow();

		return timeSlotUsage;
	}

	@RequestMapping(path = "/usage/{timeSlotId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public TimeSlotUsage createSlotUsage(@PathVariable("timeSlotId") Integer timeSlotId) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		TimeSlotUsage timeSlotUsage = timeSlotService.create(user, timeSlotId);

		return timeSlotUsage;
	}

	@RequestMapping(path = "/usage/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<TimeSlotUsage> getAllTimeSlotUsagesForTommorow(
			@RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "ddMMyyyy") Date fromDate,
			@RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "ddMMyyyy") Date toDate) {

		List<TimeSlotUsage> timeSlotUsage = timeSlotService.findAllTimeSlotUsage(fromDate, toDate);

		return timeSlotUsage;
	}

}
