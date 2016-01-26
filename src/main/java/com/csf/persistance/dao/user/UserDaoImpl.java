package com.csf.persistance.dao.user;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csf.persistance.dao.JpaDao;
import com.csf.persistence.entity.User;

@Repository("userDao")
public class UserDaoImpl extends JpaDao<User, Integer> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findUserByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("Korisnicko ime " + username + " ne postoji");
		}

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

		Root<User> root = criteriaQuery.from(this.entityClass);
		Path<String> usernamePath = root.get("username");
		criteriaQuery.where(builder.equal(usernamePath, username));

		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> users = typedQuery.getResultList();

		if (users.isEmpty()) {
			return null;
		}

		return users.iterator().next();
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

		Root<User> root = criteriaQuery.from(this.entityClass);
		Path<String> emailPath = root.get("email");
		criteriaQuery.where(builder.equal(emailPath, email));

		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> users = typedQuery.getResultList();

		if (users.isEmpty()) {
			return null;
		}

		return users.iterator().next();
	}

	@Override
	public User changeUserRole(Integer id, Integer userRole) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaUpdate<User> update = builder.createCriteriaUpdate(this.entityClass);

		Root<User> root = update.from(this.entityClass);
		update.where(builder.equal(root.get("id"), id));

		update.set(root.<Integer> get("role"), userRole);
		this.getEntityManager().createQuery(update).executeUpdate();

		return this.find(id);
	}

	@Override
	public List<User> findAllForCoach(Integer id) {
		final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

		Root<User> root = criteriaQuery.from(this.entityClass);
		Path<String> coachIdPath = root.get("coach");
		criteriaQuery.where(builder.equal(coachIdPath, id));

		TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		List<User> users = typedQuery.getResultList();

		if (users.isEmpty()) {
			return null;
		}

		return users;
	}

}
