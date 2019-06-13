package com.kuyuner.core.sys.controller;

import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.UploadFileUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("${kuyuner.admin-path}/temp")
public class TempController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @RequestMapping("file/upload")
    public ResultJson uploadFile(HttpServletRequest request) throws IOException {
        String folder = fileBasePath + "temp" + File.separator + "file" + File.separator;
        List<FileInfo> files = UploadFileUtils.uploadFileToFolder(request, folder);
        for (FileInfo file : files) {
            file.setUrl(file.getOriginFile().getAbsolutePath().replace(fileBasePath, "files/").replace("\\", "/"));
        }
        return ResultJson.ok(files);
    }
}
