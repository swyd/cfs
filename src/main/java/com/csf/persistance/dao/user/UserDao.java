package com.csf.persistance.dao.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.csf.persistance.dao.GenericDao;
import com.csf.persistence.entity.User;

public interface UserDao extends GenericDao<User, Integer>, UserDetailsService {

	User findUserByEmail(String email) throws UsernameNotFoundException;

	User findUserByUsername(String username) throws UsernameNotFoundException;

}
