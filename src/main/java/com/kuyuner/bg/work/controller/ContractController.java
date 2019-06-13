package com.kuyuner.bg.work.controller;

import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.io.FileUtils;
import com.kuyuner.common.io.UploadFileUtils;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Contract;
import com.kuyuner.bg.work.service.ContractService;
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
 * 合同管理Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/contract")
public class ContractController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @Autowired
    private ContractService contractService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("contract")
    public String showContractList() {
        return "work/contractList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showContractForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("contract", contractService.get(id));
        } else {
            modelMap.addAttribute("contract", new Contract());
        }
        return "work/contractForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param contract
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Contract contract, String pageNum, String pageSize) {
        return contractService.findPageList(pageNum, pageSize, contract);
    }

    /**
     * 新增或修改数据
     *
     * @param contract
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save", produces = "text/html;charset=utf-8")
    public String save(HttpServletRequest request, Contract contract) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String folder = fileBasePath + "approval_files" + File.separator + dateFormat.format(new Date()) + File.separator;
        List<FileInfo> list = UploadFileUtils.uploadFileToFolder(request, folder);
        if (list != null && list.size() > 0) {
            FileInfo fileInfo = list.get(0);
            contract.setFilePath(fileInfo.getOriginFile().getAbsolutePath().replace(fileBasePath, "files/").replace("\\", "/"));
            contract.setFileSize(fileInfo.getSize());
            contract.setFileName(fileInfo.getName());
        }
        return JsonMapper.toJsonString(contractService.saveOrUpdate(contract));
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
        return contractService.deletes(ids.split(","));
    }

    @RequestMapping("download/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        Contract file = contractService.get(id);
        if (file != null) {
            WebUtil.setResponseFileName(request, response, file.getFileName());
            FileUtils.copyFile(new File(fileBasePath + file.getFilePath().replaceFirst("files", "")), response.getOutputStream());
        }
    }
}