package com.csf.persistance.dao.timeslot;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	@Override
	public List<TimeSlot> findAllActiveForType(Integer type) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<TimeSlot> criteriaQuery = builder.createQuery(this.entityClass);
		Root<TimeSlot> root = criteriaQuery.from(this.entityClass);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.<Boolean> get("isActive"), Boolean.TRUE));
		predicates.add(builder.equal(root.<Integer> get("type"), type));

		criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.<Integer> get("priority")));

		List<TimeSlot> tsu = this.getEntityManager().createQuery(criteriaQuery).getResultList();
		return tsu;
	}

	@Override
	public boolean checkIfExists(Integer type, String startsAt) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<TimeSlot> root = criteriaQuery.from(this.entityClass);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.<Integer> get("type"), type));
		predicates.add(builder.equal(root.<String> get("startsAt"), startsAt));

		criteriaQuery.select(builder.count(root)).where(predicates.toArray(new Predicate[] {}));

		Long tsu = this.getEntityManager().createQuery(criteriaQuery).getSingleResult();
		if (tsu > 0) {
			return true;
		} else {
			return false;
		}
	}

}
