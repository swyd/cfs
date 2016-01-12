package com.csf.api.rest.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.csf.api.rest.transfer.model.NewsTransfer;
import com.csf.api.rest.transfer.model.TimeSlotTransfer;
import com.csf.api.rest.transfer.model.TimeSlotUsageTransfer;
import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistence.entity.News;
import com.csf.persistence.entity.TimeSlot;
import com.csf.persistence.entity.User;

public class TransferConverterUtil {

	public static List<UserTransfer> convertAllUsersToTransfer(List<User> users) {
		List<UserTransfer> usersTransfer = new ArrayList<UserTransfer>();
		for (User user : users) {
			usersTransfer.add(convertUserToTransfer(user));
		}
		return usersTransfer;

	}

	public static UserTransfer convertUserToTransfer(User user) {//remove password from this
		return new UserTransfer(user.getUsername(), user.getId(), user.getName(), createRoleMap(user.getAuthorities()));
	}

	private static Map<String, Boolean> createRoleMap(Collection<? extends GrantedAuthority> collection) {
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : collection) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}
	
	public static List<NewsTransfer> convertNewsToTransfer(List<News> news){
		List<NewsTransfer> newsTransfer = new ArrayList<NewsTransfer>();

			for (News article : news) {
				newsTransfer.add(convertNewsToTransfer(article));

		}

		return newsTransfer;
	}
	
	public static NewsTransfer convertNewsToTransfer(News article){
		
		return null;
	}
	
	public static List<TimeSlotTransfer> convertTimeSlotToTransfer(List<TimeSlot> timeSlots){
		List<TimeSlotTransfer> timeSlotsTransfer = new ArrayList<TimeSlotTransfer>();

			for (TimeSlot timeSlot : timeSlots) {
				timeSlotsTransfer.add(convertTimeSlotToTransfer(timeSlot));

		}

		return timeSlotsTransfer;
	}
	
	public static TimeSlotTransfer convertTimeSlotToTransfer(TimeSlot timeSlot){
		TimeSlotTransfer timeSlotTransfer = new TimeSlotTransfer();
		timeSlotTransfer.setId(timeSlot.getId());
		timeSlotTransfer.setLimit(timeSlot.getLimit());
		timeSlotTransfer.setStartsAt(timeSlot.getStartsAt());
		return timeSlotTransfer;
	}
	
//	public static TimeSlotTransfer convertToPacketTransfer(TimeSlotUsage packet) {
//		TimeSlotTransfer packetTransfer = new TimeSlotTransfer();
//		packetTransfer.setName(packet.getName());
//		packetTransfer.setPrice(packet.getPrice());
//		packetTransfer.setUsage(packet.getUsage());
//		packetTransfer.setId(packet.getId());
//		packetTransfer.setIsActive(packet.getIsActive());
//		return packetTransfer;
//	}
//
//	public static NewsTransfer convertToContractTransfer(News contract) {
//		NewsTransfer contractTransfer = new NewsTransfer();
//		
//		contractTransfer.setId(contract.getId());
//		contractTransfer.setIme(contract.getName());
//		contractTransfer.setEmail(contract.getEmail());
//		contractTransfer.setBrojTelefona(contract.getPhoneNumber());
//		contractTransfer.setAdresa(contract.getAddress());
//		
//		return new NewsTransfer();
//	}
//	
//	public static List<NewsTransfer> convertAllContractsToTransfer(List<News> contracts){
//		List<NewsTransfer> contractTransfer = new ArrayList<NewsTransfer>();
//		for (News contract : contracts) {
//			contractTransfer.add(convertToContractTransfer(contract));
//		}
//		return contractTransfer;
//	}
	
	public static List<TimeSlotUsageTransfer> convertAllContractUsageToTransfer(List<TimeSlot> contractsUsage){
		List<TimeSlotUsageTransfer> contractUsageTransfer = new ArrayList<TimeSlotUsageTransfer>();
		for (TimeSlot contractUsage : contractsUsage) {
			contractUsageTransfer.add(convertContractUsageToTransfer(contractUsage));
		}
		return contractUsageTransfer;
	}
	
	public static TimeSlotUsageTransfer convertContractUsageToTransfer(TimeSlot contractsUsage){
		//TODO implement logic
		return new TimeSlotUsageTransfer();
	}
}
