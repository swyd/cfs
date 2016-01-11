package com.csf.service.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistence.entity.User;

public interface UserService {

	List<User> findAll();

	User find(Integer id);

	User save(User user);

	void delete(Integer id);

	User findUserByUsername(String email);

	UserDetails loadUserByUsername(String email);

	User changePassword(User user, String password);

	User update(UserTransfer userTransfer, User user);

}
