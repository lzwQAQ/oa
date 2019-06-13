package com.kuyuner.common.io;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传工具类
 *
 * @author Administrator
 */
public class UploadFileUtils {

    public static List<FileInfo> uploadFileToFolder(HttpServletRequest request, String folder) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!multipartResolver.isMultipart(request)) {
            return null;
        }

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator iter = multiRequest.getFileNames();
        List<FileInfo> files = new ArrayList<>();
        while (iter.hasNext()) {
            MultipartFile multipartFile = multiRequest.getFile(iter.next().toString());
            if (multipartFile != null) {
                String extName = "." + StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
                File file = new File(folder + IdGenerate.uuid() + extName);

                File fileFolder = file.getParentFile();
                if (!fileFolder.exists()) {
                    fileFolder.mkdirs();
                }

                multipartFile.transferTo(file);

                FileInfo info = new FileInfo();
                info.setName(multipartFile.getOriginalFilename());
                info.setSuffix(extName);
                info.setSize(file.length());
                info.setOriginFile(file);
                files.add(info);
            }
        }

        return files;
    }
}
