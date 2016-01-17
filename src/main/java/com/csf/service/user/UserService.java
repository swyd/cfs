package com.csf.service.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistence.entity.User;

public interface UserService {

	List<User> findAll();

	User find(Integer id);

	void delete(Integer id);

	User findUserByUsername(String email);

	UserDetails loadUserByUsername(String email);

	User changePassword(User user, String password, String newPassword);

	User update(UserTransfer userTransfer, User user);

	User save(User user, Boolean encode);

	User save(UserTransfer userTransfer);

}
