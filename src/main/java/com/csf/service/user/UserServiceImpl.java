package com.csf.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistance.dao.timeslot.TimeSlotUsageDao;
import com.csf.persistance.dao.user.UserDao;
import com.csf.persistence.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TimeSlotUsageDao usageDao;

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User find(Integer id) {
		return userDao.find(id);
	}

	@Override
	public User save(User user, Boolean encode) {
		if (encode) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return userDao.save(user);
	}

	@Override
	public void delete(Integer id) {
		// check if just set to inactive
		usageDao.removeAllSessionsForUser(id);
		userDao.delete(id);
	}

	@Override
	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.findUserByUsername(username);
	}

	@Override
	public User changePassword(User user, String oldPassword, String newPassword) {
		//add more validations
		if(!passwordEncoder.matches(oldPassword, user.getPassword())){
			throw new RestException("Old password doesn't match the existing one");
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		return userDao.save(user);
	}

	@Override
	public User update(UserTransfer userTransfer, User user) {
		if (userTransfer.getUsername() != null) {
			user.setUsername(userTransfer.getUsername());
		}
		if (userTransfer.getIsActive() != null) {
			user.setIsActive(userTransfer.getIsActive());
		}
		if (userTransfer.getName() != null) {
			user.setName(userTransfer.getName());
		}
		if (userTransfer.getSurname() != null) {
			user.setSurname(userTransfer.getSurname());
		}
		if (userTransfer.getIsAdmin() != null) {
			user.setIsAdmin(userTransfer.getIsAdmin());
		}
		if (userTransfer.getDatePaid() != null) {
			user.setDatePaid(userTransfer.getDatePaid());
		}
		if (userTransfer.getDateExpiring() != null) {
			user.setDateExpiring(userTransfer.getDateExpiring());
		}
		if (userTransfer.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(userTransfer.getPassword()));
		}
		if (userTransfer.getEmail() != null) {
			user.setEmail(userTransfer.getEmail());
		}
		if (userTransfer.getSessionsLeft() != null) {
			user.setSessionsLeft(userTransfer.getSessionsLeft());
		}
		user.setId(userTransfer.getId());
		return userDao.save(user);
	}

	@Override
	public User save(UserTransfer userTransfer) {
		User user = new User();
		user.setPassword(userTransfer.getPassword());

		return null;
	}

}
