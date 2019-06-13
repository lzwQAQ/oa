package com.kuyuner.bg.article.service;


import com.kuyuner.bg.article.entity.Article;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ArticleService {

	Map getHomePageData();

	String savePicture(MultipartFile picture);

	ResultJson saveOrUpdate(Article article);

	PageJson findArticleList(String pageNum, String pageSize, Article article);

	ResultJson deletes(String[] ids);

	ResultJson getDispatch(String userId);

	ResultJson getLeave(String userId);

	ResultJson getBusiness(String userId);

	ResultJson getPurchase(String userId);

	ResultJson getEmail(String userId);

	Article articleDetail(String id);
}
