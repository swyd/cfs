package com.csf.api.rest.resources;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.NewsTransfer;
import com.csf.persistence.entity.News;
import com.csf.service.news.NewsService;

@RestController
@RequestMapping("/news")
public class NewsResource {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NewsService newsService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public List<NewsTransfer> getNews() {

		List<News> news = newsService.findAll();
		return TransferConverterUtil.convertNewsToTransfer(news);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}", produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public NewsTransfer getArticle(@PathVariable("id") Integer id) {

		News article = newsService.find(id);
		return TransferConverterUtil.convertNewsToTransfer(article);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public NewsTransfer createArticle(@RequestBody News article) {

		News retVal = newsService.save(article);

		return TransferConverterUtil.convertNewsToTransfer(retVal);

	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public NewsTransfer updateArticle(@RequestBody News article) {

		News retVal = newsService.save(article);

		return TransferConverterUtil.convertNewsToTransfer(retVal);
	}

}
