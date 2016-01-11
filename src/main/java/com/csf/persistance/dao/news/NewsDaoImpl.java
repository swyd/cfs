package com.csf.persistance.dao.news;

import org.springframework.stereotype.Repository;

import com.csf.persistance.dao.JpaDao;
import com.csf.persistence.entity.News;

@Repository
public class NewsDaoImpl extends JpaDao<News, Integer> implements NewsDao {

	public NewsDaoImpl(){
		super(News.class);
	}

}
