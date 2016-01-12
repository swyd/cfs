package com.csf.persistance.dao.timeslot;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.csf.persistance.dao.JpaDao;
import com.csf.persistence.entity.TimeSlotUsage;

@Repository
@Transactional
public class TimeSlotUsageDaoImpl extends JpaDao<TimeSlotUsage, Integer> implements TimeSlotUsageDao {

	public TimeSlotUsageDaoImpl() {
		super(TimeSlotUsage.class);
	}

	@Override
	public List<TimeSlotUsage> findAll(Date fromDate, Date toDate) {
		// TODO Create named query and call it
		return this.findAll();
	}

	@Override
	public List<TimeSlotUsage> findAllForDate(Date date) {
		List<TimeSlotUsage> timeSlotUsage = this.getEntityManager()
				.createNamedQuery("TimeSlotUsage.findAllForDate", TimeSlotUsage.class).setParameter("usageDate", date, TemporalType.DATE)
				.getResultList();

		return timeSlotUsage;
	}

}
