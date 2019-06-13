package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.approval.service.ApprovalFileService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.io.FileUtils;
import com.kuyuner.common.network.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @create 2018-12-05
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/approval")
public class ApprovalController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @Autowired
    private ApprovalFileService fileService;

    @RequestMapping("download/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ApprovalFile file = fileService.getFile(id);
        if (file != null) {
            WebUtil.setResponseFileName(request, response, file.getFileName());
            FileUtils.copyFile(new File(fileBasePath + file.getFilePath().replaceFirst("files", "")), response.getOutputStream());
        }
    }
}
