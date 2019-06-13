package com.kuyuner.bg.work.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Train;
import com.kuyuner.bg.work.service.TrainService;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 培训通知Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/train")
public class TrainController extends BaseController {

    @Autowired
    private TrainService trainService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("train")
    public String showTrainList() {
        return "work/trainList";
    }

    /**
     * 发起培训
     *
     * @return
     */
    @RequestMapping("trainsend")
    public String trainSend() {
        return "work/trainSend";
    }

    /**
     * 显示表单页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showTrainForm(ModelMap modelMap) {
        Train train = new Train();
        train.setTrainer(UserUtils.getUser());
        modelMap.addAttribute("train", train);
        return "work/trainForm";
    }

    /**
     * 显示详情页面
     *
     * @return
     */
    @RequestMapping("form/{id}")
    public String showTrainDetail(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        Train train = trainService.get(id);
        modelMap.addAttribute("train", train);
        modelMap.addAttribute("joinPeoples", trainService.findJoinPeoples(train.getJoinPeople().split(",")));
        return "work/trainDetail";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param train
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Train train, String pageNum, String pageSize) {
        return trainService.findPageList(pageNum, pageSize, train);
    }

    /**
     * 发起培训列表
     *
     * @param train
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("sendlist")
    public PageJson sendList(Train train, String pageNum, String pageSize) {
        return trainService.findSendPageList(pageNum, pageSize, train);
    }

    /**
     * 新增或修改数据
     *
     * @param train
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Train train) {
        return trainService.saveOrUpdate(train);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson delete(String ids) {
        return trainService.deletes(ids.split(","));
    }

    /**
     * 查询用户树
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("usertree")
    public ListJson userTree() {
        return trainService.findUserTree();
    }
}