package com.kuyuner.bg.recanddis.controller;

import com.kuyuner.bg.recanddis.entity.ReceiveDocument2;
import com.kuyuner.bg.recanddis.service.ReceiveDocumentService2;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 收文第二套功能
 *
 * @author admin
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/receivedocument2")
public class ReceiveDocumentController2 {

    @Autowired
    private ReceiveDocumentService2 receiveDocumentService2;


    /**
     * 显示代办列表
     *
     * @return
     */
    @RequestMapping("receivedocument2")
    public String showReceiveDocument() {
        return "recanddis/receivedocument2/receivedocumentList";
    }

    /**
     * 显示收藏夹列表页面
     *
     * @return
     */
    @RequestMapping("favorites")
    public String showFavorite() {
        return "recanddis/receivedocument2/receivedocumentFavoritesList";
    }

    /**
     * 显示代办列表
     *
     * @return
     */
    @RequestMapping("detail/{id}")
    public String showDetail(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("document", receiveDocumentService2.get(id));
        modelMap.addAttribute("approvalFile", receiveDocumentService2.getApprovalFile(id));
        return "recanddis/receivedocument2/receivedocumentDetail";
    }

    /**
     * 查询自己所有的文件
     *
     * @param receiveDocument
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson showList(ReceiveDocument2 receiveDocument, String pageNum, String pageSize) {
        return receiveDocumentService2.findPageList(pageNum, pageSize, receiveDocument);
    }

    /**
     * 查询收藏夹
     *
     * @param receiveDocument
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("favorites/list")
    public PageJson showFavoriteList(ReceiveDocument2 receiveDocument, String pageNum, String pageSize) {
        receiveDocument.setStar("1");
        return receiveDocumentService2.findPageList(pageNum, pageSize, receiveDocument);
    }

    /**
     * 收藏或取消收藏
     *
     * @param id
     * @param star
     * @return
     */
    @ResponseBody
    @RequestMapping("star/{id}")
    public ResultJson star(@PathVariable("id") String id, String star) {
        ReceiveDocument2 receiveDocument2 = new ReceiveDocument2();
        receiveDocument2.setId(id);
        receiveDocument2.setStar("1".equals(star) ? "1" : "0");
        receiveDocumentService2.update(receiveDocument2);
        return ResultJson.ok("1".equals(star) ? "收藏成功！" : "取消成功！");
    }

}
