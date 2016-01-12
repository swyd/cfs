package com.csf.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistance.dao.user.UserDao;
import com.csf.persistence.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User find(Integer id) {
		return userDao.find(id);
	}

	@Override
	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDao.save(user);
	}

	@Override
	public void delete(Integer id) {
		// check if just set to inactive
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
	public User changePassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		return userDao.save(user);
	}

	@Override
	public User update(UserTransfer userTransfer, User user) {
		//TODO implement
		return userDao.save(user);
	}

}
