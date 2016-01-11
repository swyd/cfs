package com.csf.service.news;

import java.util.List;

import com.csf.persistence.entity.News;

public interface NewsService {

	  List<News> findAll();
	  
	  News find(Integer id);
	  
	  News save(News timeSlotUsage);
}
