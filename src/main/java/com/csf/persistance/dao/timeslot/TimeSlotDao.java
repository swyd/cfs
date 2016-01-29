package com.csf.persistance.dao.timeslot;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.csf.persistance.dao.GenericDao;
import com.csf.persistence.entity.TimeSlot;

@Repository
public interface TimeSlotDao extends GenericDao<TimeSlot, Integer> {

	List<TimeSlot> findAllActiveForType(Integer type);

	boolean checkIfExists(Integer type, String startsAt);

}
