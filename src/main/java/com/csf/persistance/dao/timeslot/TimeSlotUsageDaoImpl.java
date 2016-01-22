package com.csf.persistance.dao.timeslot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.csf.persistance.dao.JpaDao;
import com.csf.persistence.entity.TimeSlotUsage;
import com.csf.persistence.entity.User;

@Repository
@Transactional
public class TimeSlotUsageDaoImpl extends JpaDao<TimeSlotUsage, Integer> implements TimeSlotUsageDao {

	public TimeSlotUsageDaoImpl() {
		super(TimeSlotUsage.class);
	}

	@Override
	public List<TimeSlotUsage> findAll(Date fromDate, Date toDate) {
		return this.findAll();
	}

	@Override
	public List<TimeSlotUsage> findAllForDate(Date date) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<TimeSlotUsage> criteriaQuery = builder.createQuery(this.entityClass);
		Root<TimeSlotUsage> root = criteriaQuery.from(this.entityClass);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (date != null) {
			predicates.add(builder.equal(root.<Date> get("usageDate"), date));
		}

		criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));

		List<TimeSlotUsage> tsu = this.getEntityManager().createQuery(criteriaQuery).getResultList();
		return tsu;
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
		Long remaining = this.getEntityManager()
				.createNamedQuery("TimeSlotUsage.getSessionsRemainingForDateAndSlot", Long.class)
				.setParameter("forDate", forDate, TemporalType.DATE).setParameter("timeSlotId", timeSlotId)
				.getSingleResult();

		return remaining;
	}

	@Override
	public List<TimeSlotUsage> findAllTimeSlotUsageFromTo(Date fromDate, Date toDate) {
		List<TimeSlotUsage> timeSlotUsage = this.getEntityManager()
				.createNamedQuery("TimeSlotUsage.findAllUsageFromTo", TimeSlotUsage.class)
				.setParameter("fromDate", fromDate, TemporalType.DATE).setParameter("toDate", toDate, TemporalType.DATE)
				.getResultList();

		return timeSlotUsage;
	}

	@Override
	public void removeAllSessionsForUser(Integer id) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaDelete<TimeSlotUsage> deleteQuery = builder.createCriteriaDelete(this.entityClass);
		Root<TimeSlotUsage> root = deleteQuery.from(this.entityClass);
		
		deleteQuery.where(builder.equal(root.<User>get("user").<Integer>get("id"), id));

		
		this.getEntityManager().createQuery(deleteQuery).executeUpdate();
	}

}
