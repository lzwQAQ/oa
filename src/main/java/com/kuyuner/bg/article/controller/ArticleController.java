package com.kuyuner.bg.article.controller;

import com.kuyuner.bg.article.entity.Article;
import com.kuyuner.bg.article.service.ArticleService;
import com.kuyuner.bg.msg.enums.ArticleTypeEnum;
import com.kuyuner.bg.recanddis.entity.FileTemplete;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.UploadFileUtils;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.security.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会议通知Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 图文列表页
     * @return
     */
    @RequestMapping("list")
    public String articleList(String type, ModelMap modelMap) {
        if (StringUtils.isNotBlank(type)) {
            modelMap.addAttribute("type", type);
            return "article/articleListView";
        }
        return "article/articleList";
    }

    /**
     * 图文编辑
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String articleForm(@PathVariable(name = "id", required = false) String id, String type, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            Article article = articleService.articleDetail(id);
            modelMap.addAttribute("article", article);
            modelMap.addAttribute("id", id);
        } else {
            modelMap.addAttribute("article", new Article());
            modelMap.addAttribute("fromId", null);
        }
        if("slideshow".equals(type))
            return "article/slideShowForm";
        else
            return "article/articleForm";
    }

    /**
     * 图文查看
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"view/{id}"})
    public String articleView(@PathVariable(name = "id", required = true) String id, ModelMap modelMap) {
        Article article = articleService.articleDetail(id);
        modelMap.addAttribute("article", article);
        return "article/articleView";
    }

    /**
     * 新增或修改数据
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(HttpServletRequest request, Article article, String editorValue) throws IOException {
        if (StringUtils.isNotBlank(editorValue)) {
            article.setContent(editorValue);
        }
        return articleService.saveOrUpdate(article);
    }

    /**
     * 彻底删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson deletes(String ids) {
        return articleService.deletes(ids.split(","));
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/articleList")
    public PageJson sendList(Article article, String pageNum, String pageSize) {
        return articleService.findArticleList(pageNum, pageSize, article);
    }

    /**
     * ['listDispatch', 'listLeave', 'listBusiness', 'listPurchase', 'listEmail']
     * 发文
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/listDispatch")
    public ResultJson listDispatch(Article article, String pageNum, String pageSize) {
        return articleService.getDispatch(UserUtils.getPrincipal().getId());
    }

    /**
     * 请假
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/listLeave")
    public ResultJson listLeave(Article article, String pageNum, String pageSize) {
        return articleService.getLeave(UserUtils.getPrincipal().getId());
    }

    /**
     * 业务
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/listBusiness")
    public ResultJson listBusiness(Article article, String pageNum, String pageSize) {
        return articleService.getBusiness(UserUtils.getPrincipal().getId());
    }

    /**
     * 采购
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/listPurchase")
    public ResultJson listPurchase(Article article, String pageNum, String pageSize) {
        return articleService.getPurchase(UserUtils.getPrincipal().getId());
    }

    /**
     * 邮件
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/listEmail")
    public ResultJson listEmail(Article article, String pageNum, String pageSize) {
        return articleService.getEmail(UserUtils.getPrincipal().getId());
    }

    //###############################H5页面调用接口########################

    /**
     * 轮播图
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/rotatePicture")
    public ResultJson rotatePicture(Article article, String pageNum, String pageSize) {
        article.setType(ArticleTypeEnum.ROTATE_PICTURE.getCode());
        return articleService.rotatePicture(article);
    }

    /**
     * 单位公告
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/announcement")
    public PageJson publicAnnouncement(Article article, String pageNum, String pageSize) {
        article.setType(ArticleTypeEnum.PUBLIC_ANNOUNCEMENT.getCode());
        return articleService.findArticleList(pageNum, pageSize, article);
    }

    /**
     * 查看文章详情
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/articleDetail")
    public ResultJson articleDetail(Article article, String pageNum, String pageSize) {
        Article result = articleService.articleDetail(article.getId());
        return ResultJson.ok(result);
    }
}