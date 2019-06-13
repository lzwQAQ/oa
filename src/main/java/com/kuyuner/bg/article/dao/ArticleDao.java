package com.kuyuner.bg.article.dao;

import com.kuyuner.bg.article.entity.Article;
import com.kuyuner.bg.recanddis.bean.ReceiveDocumentHistoricListView;
import com.kuyuner.bg.recanddis.bean.ReceiveDocumentPendingListView;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {
    List<Article> findHomePageData(String type);

    Article articleDetail(String id);
}