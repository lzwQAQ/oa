package com.kuyuner.bg.recanddis.controller;

import com.kuyuner.bg.recanddis.entity.OfficialSeal;
import com.kuyuner.bg.recanddis.service.OfficialSealService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.FileUtils;
import com.kuyuner.common.io.UploadFileUtils;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.network.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 发文公章Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/officialseal")
public class OfficialSealController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @Autowired
    private OfficialSealService officialSealService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("officialseal")
    public String showOfficialSealList() {
        return "recanddis/officialSealList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showOfficialSealForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("officialSeal", officialSealService.get(id));
        } else {
            modelMap.addAttribute("officialSeal", new OfficialSeal());
        }
        return "recanddis/officialSealForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param officialSeal
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(OfficialSeal officialSeal, String pageNum, String pageSize) {
        return officialSealService.findPageList(pageNum, pageSize, officialSeal);
    }

    /**
     * 新增或修改数据
     *
     * @param officialSeal
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save", produces = "text/html;charset=utf-8")
    public String save(HttpServletRequest request, OfficialSeal officialSeal) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String folder = fileBasePath + "approval_files" + File.separator + dateFormat.format(new Date()) + File.separator;
        List<FileInfo> list = UploadFileUtils.uploadFileToFolder(request, folder);
        if (list != null && list.size() > 0) {
            FileInfo fileInfo = list.get(0);
            officialSeal.setFilePath(fileInfo.getOriginFile().getAbsolutePath().replace(fileBasePath, "files/").replace("\\", "/"));
            officialSeal.setFileSize(fileInfo.getSize());
        }
        return JsonMapper.toJsonString(officialSealService.saveOrUpdate(officialSeal));
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
        return officialSealService.deletes(ids.split(","));
    }

    @RequestMapping("download/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("id") String id) throws IOException {
        OfficialSeal file = officialSealService.get(id);
        if (file != null) {
            WebUtil.setResponseFileName(request, response, file.getFileName());
            FileUtils.copyFile(new File(fileBasePath + file.getFilePath().replaceFirst("files", "")), response.getOutputStream());
        }
    }
}