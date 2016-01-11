package com.csf.persistance.dao.timeslot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csf.persistance.dao.JpaDao;
import com.csf.persistence.entity.TimeSlot;

@Repository
@Transactional
public class TimeSlotDaoImpl extends JpaDao<TimeSlot, Integer> implements TimeSlotDao {

	public TimeSlotDaoImpl() {
		super(TimeSlot.class);
	}


}
