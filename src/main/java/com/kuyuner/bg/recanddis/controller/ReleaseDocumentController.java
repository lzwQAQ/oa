package com.kuyuner.bg.recanddis.controller;

import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.approval.service.ApprovalFileService;
import com.kuyuner.bg.recanddis.entity.ReleaseDocument;
import com.kuyuner.bg.recanddis.service.ReleaseDocumentService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.UploadFileUtils;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 发文Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/releasedocument")
public class ReleaseDocumentController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @Autowired
    private ReleaseDocumentService releaseDocumentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApprovalFileService fileService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "recanddis/releasedocument/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "recanddis/releasedocument/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "recanddis/releasedocument/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showReleaseDocumentForm(String businessId, String businessLogId, String type,
                                          String firstTask, ModelMap modelMap, String taskName) {
        ApprovalFile file = null;
        if ("historic".equals(type)) {
            modelMap.addAttribute("releaseDocument", releaseDocumentService.getLog(businessLogId));
            file = fileService.findLogFiles(businessLogId).get(0);
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("releaseDocument", releaseDocumentService.get(businessId));
                file = fileService.findFiles(businessId).get(0);
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                ReleaseDocument releaseDocument = new ReleaseDocument();
                releaseDocument.setSenderName(user.getName());
                releaseDocument.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("releaseDocument", releaseDocument);
            }
        }
        modelMap.addAttribute("editFile", "create".equals(type) || ("pending".equals(type) && "true".equals(firstTask)));
        modelMap.addAttribute("approvalFile", file);
        List<User> userList = null;
        if ("发布".equals(taskName)) {
            userList = releaseDocumentService.findUserList();
        }
        modelMap.addAttribute("userList", userList);
        return "recanddis/releasedocument/taskForm";
    }


    /**
     * 查询待办数据
     *
     * @param releaseDocument
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(ReleaseDocument releaseDocument, String pageNum, String pageSize) {
        return releaseDocumentService.findPendingList(pageNum, pageSize, releaseDocument);
    }

    /**
     * 查询审批历史
     *
     * @param releaseDocument
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(ReleaseDocument releaseDocument, String pageNum, String pageSize) {
        return releaseDocumentService.findHistoricList(pageNum, pageSize, releaseDocument);
    }

    /**
     * 查询申请历史
     *
     * @param releaseDocument
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(ReleaseDocument releaseDocument, String pageNum, String pageSize) {
        return releaseDocumentService.findSendList(pageNum, pageSize, releaseDocument);
    }

    /**
     * 提交申请
     *
     * @param releaseDocument
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "submit", produces = "text/html;charset=utf-8")
    public String submit(HttpServletRequest request, ReleaseDocument releaseDocument, String taskResult) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String folder = fileBasePath + "approval_files" + File.separator + dateFormat.format(new Date()) + File.separator;
        List<FileInfo> list = UploadFileUtils.uploadFileToFolder(request, folder);
        FileInfo fileInfo = null;
        if (list != null && list.size() > 0) {
            fileInfo = list.get(0);
            fileInfo.setUrl(fileInfo.getOriginFile().getAbsolutePath().replace(fileBasePath, "files/").replace("\\", "/"));
        }
        ResultJson resultJson = releaseDocumentService.submitForm(releaseDocument, fileInfo, taskResult);
        return JsonMapper.toJsonString(resultJson);
    }

    /**
     * 审批
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(String id, String approvalResult, String taskResult, String taskName, String sendTo) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return releaseDocumentService.approvalForm(id, approvalResult, taskResult, taskName, sendTo);
    }


}