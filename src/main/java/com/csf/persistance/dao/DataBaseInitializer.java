package com.csf.persistance.dao;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.csf.persistance.dao.user.UserDao;


/**
 * Initialize the database with some test entries.
 * 
 */
public class DataBaseInitializer {

  private UserDao userDao;

  private PasswordEncoder passwordEncoder;


  protected DataBaseInitializer() {
    /* Default constructor for reflection instantiation */
  }


//  public void initDataBase() {
//    User userUser = new User("user", this.passwordEncoder.encode("user"));
//    userUser.addRole("ROLE_USER");
//    this.userDao.save(userUser);
//
//    User adminUser = new User("admin", this.passwordEncoder.encode("admin"));
//    adminUser.addRole("ROLE_USER");
//    adminUser.addRole("ROLE_ADMIN");
//    this.userDao.save(adminUser);
//
//    long timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
//    for (int i = 0; i < 10; i++) {
//      NewsEntry newsEntry = new NewsEntry();
//      newsEntry.setContent("This is example content " + i);
//      newsEntry.setDate(new Date(timestamp));
//      this.newsEntryDao.save(newsEntry);
//      timestamp += 1000 * 60 * 60;
//    }
//  }

}
