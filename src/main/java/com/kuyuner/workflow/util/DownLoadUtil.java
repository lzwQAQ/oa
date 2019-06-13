package com.kuyuner.workflow.util;

import com.kuyuner.common.idgen.IdGenerate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tangzj
 */
public class DownLoadUtil {

    /**
     * 提供bpmnmodel的下载功能
     *
     * @param request
     * @param response
     * @param list
     */
    public static void downloadForBpmn(HttpServletRequest request, HttpServletResponse response,
                                       List<Map<String, String>> list) {

        File downLoadFile = null;
        String downLoadFileName = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            List<Map<String, String>> values = new ArrayList<Map<String, String>>();
            for (Map<String, String> map : list) {
                String key = map.keySet().iterator().next();
                String bpmnModel = map.get(key);
                Map<String, String> m = new HashMap<String, String>();
                m.put(key, bpmnModel);
                values.add(m);
            }

            // 检查是否要压缩成zip文件
            if (values.size() > 1) {
                String baseTepmPath = request.getServletContext().getRealPath("/");
                downLoadFile = new File(baseTepmPath + IdGenerate.uuid() + ".zip");
                ZipCompress zipCompress = new ZipCompress(downLoadFile);
                for (Map<String, String> map : values) {
                    String key = map.keySet().iterator().next();
                    zipCompress.compress(map.get(key).getBytes("UTF-8"), key + "json");
                }
                zipCompress.close();
                inputStream = new FileInputStream(downLoadFile);
                downLoadFileName = downLoadFile.getName();
            } else {
                Map<String, String> map = values.get(0);
                String key = map.keySet().iterator().next();
                inputStream = new ByteArrayInputStream(map.get(key).getBytes("UTF-8"));
                downLoadFileName = key + "json";
            }

            // 下载文件
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + encodeFilename(downLoadFileName, request));
            int l;
            byte[] bytes = new byte[1024];
            outputStream = response.getOutputStream();
            while ((l = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, l);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            FileUtils.deleteQuietly(downLoadFile);
        }
    }

    /**
     * 根据浏览器类型进行文件名称进行编码转换
     *
     * @param filename
     * @param request
     * @return
     */
    private static String encodeFilename(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
         * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
         * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla"))) {
                return new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }
}
