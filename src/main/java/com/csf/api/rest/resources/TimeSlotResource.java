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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.StringTransfer;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
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

	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public List<TimeSlotTransfer> getAllTimeSlots() {
		return TransferConverterUtil.convertTimeSlotToTransfer(timeSlotService.findAll());
	}

	@RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public TimeSlotTransfer createTimeslot(@RequestBody TimeSlotTransfer timeSlotTransfer) {
		
		return null;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public StringTransfer deleteTimeslot(@PathVariable("id") Integer id) {
		timeSlotService.delete(id);
		return new StringTransfer("TimeSlot deleted successfully");
	}
}
