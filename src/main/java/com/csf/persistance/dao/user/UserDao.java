package com.csf.persistance.dao.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.csf.persistance.dao.GenericDao;
import com.csf.persistence.entity.User;

public interface UserDao extends GenericDao<User, Integer>, UserDetailsService {

	User findUserByEmail(String email) throws UsernameNotFoundException;

	User findUserByUsername(String username) throws UsernameNotFoundException;

	User changeUserRole(Integer id, Integer userRole);
	
	List<User> findAllForCoach(Integer id);

}
