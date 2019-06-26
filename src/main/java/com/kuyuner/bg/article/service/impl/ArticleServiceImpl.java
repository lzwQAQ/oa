package com.kuyuner.bg.article.service.impl;

import com.kuyuner.bg.article.dao.*;
import com.kuyuner.bg.article.entity.*;
import com.kuyuner.bg.article.service.ArticleService;
import com.kuyuner.bg.article.utils.DateUtil;
import com.kuyuner.bg.article.utils.ImageUtil;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.persistence.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private DispatchHomeDao dispatchHomeDao;
	@Autowired
	private LeaveHomeDao leaveDao;
	@Autowired
	private BusinessHomeDao businessDao;
	@Autowired
	private PurchaseHomeDao purchaseHomeDao;
	@Autowired
	private EmailHomeDao emailDao;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Map getHomePageData() {
		//1.轮播图2.单位公告3.重点专题4.期刊分享5.两学一做6.共建和谐7.党务文献8.专业知识
		Map result = new HashMap();
		setHomepageResult(result);
		return result;
	}

	@Override
	public String savePicture(MultipartFile picture) {
		String path = "/interest/" + DateUtil.currentTimes();
		String pictureUrl = null;
		try {
			if (picture != null) {
				String fileName = ImageUtil.saveImg(picture, "D:/bg/file/image" + path);
				pictureUrl = fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pictureUrl;
	}

	@Override
	public ResultJson saveOrUpdate(Article article) {
		int count;
		if (StringUtils.isBlank(article.getId())) {
			article.setId(IdGenerate.uuid());
			count = articleDao.insert(article);
		} else {
			count = articleDao.update(article);
		}
		return count > 0 ? ResultJson.ok() : ResultJson.failed();
	}

	@Override
	public PageJson findArticleList(String pageNum, String pageSize, Article article) {
		Page<Article> page = new Page<>(pageNum, pageSize);
		page.start();
		articleDao.findList(article);
		page.end();
		return new PageJson(page);
	}

	@Override
	public ResultJson rotatePicture(Article article) {
		List<Article> articles = articleDao.findHomePageData(String.valueOf(article.getType()));;
		return ResultJson.ok(articles);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultJson deletes(String[] ids) {
		articleDao.deletes(ids);
		return ResultJson.ok();
	}

	@Override
	public ResultJson getDispatch(String userId) {
		List<DispatchHome> list = null;
		try {
			list = dispatchHomeDao.findData(userId);
		} catch (Exception e) {
			logger.error("首页发文异常" + e.getMessage());
		}
		return ResultJson.ok(list);
	}

	@Override
	public ResultJson getLeave(String userId) {
		List<LeaveHome> list = null;
		try {
			list = leaveDao.findData(userId);
		} catch (Exception e) {
			logger.error("首页请假异常" + e.getMessage());
		}
		return ResultJson.ok(list);
	}

	@Override
	public ResultJson getBusiness(String userId) {
		List<BusinessHome> list = null;
		try {
			list = businessDao.findData(userId);
		} catch (Exception e) {
			logger.error("首页请假异常" + e.getMessage());
		}
		return ResultJson.ok(list);
	}

	@Override
	public ResultJson getPurchase(String userId) {
		List<PurchaseHome> list = null;
		try {
			list = purchaseHomeDao.findData(userId);
		} catch (Exception e) {
			logger.error("首页采购异常" + e.getMessage());
		}
		return ResultJson.ok(list);
	}

	@Override
	public ResultJson getEmail(String userId) {
		List<EmailHome> list = null;
		try {
			list = emailDao.findData(userId);
		} catch (Exception e) {
			logger.error("首页邮件异常" + e.getMessage());
		}
		return ResultJson.ok(list);
	}

	@Override
	public Article articleDetail(String id) {
		Article article = null;
		try {
			article = articleDao.articleDetail(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return article;
	}

	@Override
	public NeedHandleCount needHandle(String userId) {
		NeedHandleCount needHandleCount = new NeedHandleCount();
		Integer emailCount = emailDao.dataCount(userId);
		Integer purchaseCount = purchaseHomeDao.dataCount(userId);
		Integer businessCount = businessDao.dataCount(userId);
		Integer leaveCount = leaveDao.dataCount(userId);
		Integer dispatchCount = dispatchHomeDao.dataCount(userId);
		needHandleCount.setBusinessCount(businessCount == null ? 0 : businessCount);
		needHandleCount.setDispatchCount(dispatchCount == null ? 0 : dispatchCount);
		needHandleCount.setEmailCount(emailCount == null ? 0 : emailCount);
		needHandleCount.setLeaveCount(leaveCount == null ? 0 : leaveCount);
		needHandleCount.setPurchaseCount(purchaseCount == null ? 0 : purchaseCount);
		return needHandleCount;
	}


	private void setHomepageResult(Map result){
		List<Article> image = articleDao.findHomePageData("1");
		List<Article> notice = articleDao.findHomePageData("2");
		List<Article> special = articleDao.findHomePageData("3");
		List<Article> periodical = articleDao.findHomePageData("4");
		List<Article> learn = articleDao.findHomePageData("5");
		List<Article> harmonious = articleDao.findHomePageData("6");
		List<Article> literature = articleDao.findHomePageData("7");
		List<Article> major = articleDao.findHomePageData("8");
		result.put("image",image);
		result.put("notice",notice);
		result.put("special",special);
		result.put("periodical",periodical);
		result.put("learn",learn);
		result.put("harmonious",harmonious);
		result.put("literature",literature);
		result.put("major",major);
	}

}
