package com.csf.service.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.persistance.dao.news.NewsDao;
import com.csf.persistence.entity.News;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao newsDao;

	@Override
	public List<News> findAll() {
		return newsDao.findAll();
	}

	@Override
	public News find(Integer id) {
		return newsDao.find(id);
	}

	@Override
	public News save(News news) {
		return newsDao.save(news);
	}

}
