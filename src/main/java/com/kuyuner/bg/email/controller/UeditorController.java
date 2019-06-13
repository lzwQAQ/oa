package com.kuyuner.bg.email.controller;

import com.kuyuner.bg.email.bean.UploadFile;
import com.kuyuner.bg.email.bean.UploadFileState;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.UploadFileUtils;
import com.kuyuner.common.mapper.JsonMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * @author administrator
 */
@Controller
public class UeditorController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @ResponseBody
    @RequestMapping(value = "${kuyuner.admin-path}/umeditor/uploadimage", produces = "text/html")
    public String uploadImage(HttpServletRequest request) {

        UploadFile uploadFile = new UploadFile();
        try {
            String folder = fileBasePath + "temp" + File.separator + "file" + File.separator;
            FileInfo file = UploadFileUtils.uploadFileToFolder(request, folder).get(0);
            file.setUrl(file.getOriginFile().getAbsolutePath().replace(fileBasePath, "files/").replace("\\", "/"));
            uploadFile.setName(file.getOriginFile().getName());
            uploadFile.setOriginalName(file.getName());
            uploadFile.setSize(file.getSize());
            uploadFile.setState(UploadFileState.SUCCESS);
            uploadFile.setType(file.getName().substring(file.getName().lastIndexOf(".")));
            uploadFile.setUrl(file.getUrl());
        } catch (Exception e) {
            uploadFile.setState(UploadFileState.UNKNOWN);
        }
        String callback = request.getParameter("callback");
        String result = JsonMapper.toJsonString(uploadFile);
        if (callback == null) {
            return result;
        } else {
            return "<script>" + callback + "(" + result + ")</script>";
        }
    }
}
