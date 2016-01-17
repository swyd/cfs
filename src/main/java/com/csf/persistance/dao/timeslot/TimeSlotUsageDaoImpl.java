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
				.createNamedQuery("TimeSlotUsage.findAllForDate", TimeSlotUsage.class)
				.setParameter("usageDate", date, TemporalType.DATE).getResultList();

		return timeSlotUsage;
	}

	@Override
	public Boolean checkIfExistsUsageForDate(Integer userId, Date date) {
		Long exists = this.getEntityManager().createNamedQuery("TimeSlotUsage.checkIfExistsForDate", Long.class)
				.setParameter("usageDate", date, TemporalType.DATE).setParameter("userId", userId).getSingleResult();
		if (exists > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Long getSessionsRemainingForDateAndSlot(Integer timeSlotId, Date forDate) {
		Long remaining = this.getEntityManager().createNamedQuery("TimeSlotUsage.getSessionsRemainingForDateAndSlot", Long.class)
				.setParameter("forDate", forDate, TemporalType.DATE).setParameter("timeSlotId", timeSlotId).getSingleResult();
		
		return remaining;
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageFromTo(Date fromDate, Date toDate) {
		List<TimeSlotUsage> timeSlotUsage = this.getEntityManager()
				.createNamedQuery("TimeSlotUsage.findAllUsageFromTo", TimeSlotUsage.class)
				.setParameter("fromDate", fromDate, TemporalType.DATE).setParameter("toDate", toDate, TemporalType.DATE).getResultList();

		return timeSlotUsage;
	}

	@Override
	public void removeAllSessionsForUser(Integer id) {
		this.getEntityManager()
		.createNamedQuery("TimeSlotUsage.removeAllSessionsForUser", TimeSlotUsage.class)
		.setParameter("userId", id).executeUpdate();
	}

}
